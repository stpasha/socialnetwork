package com.basebook.persistence.repository;

import com.basebook.model.Comment;
import com.basebook.model.Like;
import com.basebook.model.Post;
import com.basebook.model.PostList;
import com.basebook.repository.PostRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class DefaultPostRepository implements PostRepository {

    public static final String EMPTY_TAG = "-";
    private final JdbcTemplate jdbcTemplate;

    public DefaultPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PostList> postRowMapper = (rs, rowNum) -> {
        PostList post = PostList.PostListBuilder.postListBuilder()
            .id(rs.getLong("post_id"))
            .title(rs.getString("title"))
            .content(rs.getString("content"))
            .imageUrl(rs.getString("image_url"))
            .isDeleted(rs.getBoolean("is_deleted"))
            .commentsCount(rs.getInt("ccount"))
            .likesCount(rs.getInt("lcount"))
            .tagNames(rs.getString("concatenated_values"))
            .build();
        if (rs.getTimestamp("created_at") != null) {
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return post;
    };

    private final RowMapper<Post> oneRowMapper = (rs, rowNum) -> {
        Post post = null;
        Set<Comment> comments = new HashSet<>();
        Set<Like> likes = new HashSet<>();
        do {
            if (post == null) {
                post = Post.builder()
                        .id(rs.getLong("post_id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .imageUrl(rs.getString("image_url"))
                        .isDeleted(rs.getBoolean("is_deleted"))
                        .build();
                if (rs.getTimestamp("created_at") != null) {
                    post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    post.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
            }

            if (rs.getObject("like_id") != null) {
                Like like = mapLike(rs);
                if (like != null && !like.isDeleted()) {
                    likes.add(like);
                }
            }
            if (rs.getObject("comment_id") != null) {
                Comment comment = mapComment(rs);
                if (comment != null && !comment.isDeleted()) {
                    comments.add(comment);
                }
            }

        } while (rs.next());
        post.setLikes(likes.stream().toList());
        post.setComments(comments.stream().toList());

        return post;
    };

    private Like mapLike(ResultSet rs) throws SQLException {
        return Like.builder()
                .postId(rs.getLong("post_id"))
                .id(rs.getLong("like_id"))
                .createdAt(rs.getTimestamp("lcreated_at").toLocalDateTime())
                .isDeleted(rs.getBoolean("lis_deleted"))
                .build();
    }

    private Comment mapComment(ResultSet rs) throws SQLException {
        Comment comment = Comment.builder()
                .postId(rs.getLong("post_id"))
                .id(rs.getLong("comment_id"))
                .content(rs.getString("ccontent"))
                .isDeleted(rs.getBoolean("cis_deleted"))
                .build();

        if (rs.getTimestamp("ccreated_at") != null) {
            comment.setCreatedAt(rs.getTimestamp("ccreated_at").toLocalDateTime());
        }

        if (rs.getTimestamp("cupdated_at") != null) {
            comment.setUpdatedAt(rs.getTimestamp("cupdated_at").toLocalDateTime());
        }

        return comment;
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("INSERT INTO posts (title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?)",
                post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());
    }

    @Override
    public Optional <Post> findById(long id) {
        return jdbcTemplate.query(
                """
                        SELECT
                            p.post_id,
                            p.title,
                            p.content,
                            p.image_url,
                            p.created_at,
                            p.updated_at,
                            p.is_deleted,
                            l.like_id,
                            l.created_at AS lcreated_at,
                            l.is_deleted AS lis_deleted,
                            c.comment_id,
                            c.content AS ccontent,
                            c.created_at AS ccreated_at,
                            c.updated_at AS cupdated_at,
                            c.is_deleted AS cis_deleted
                        FROM posts p
                            LEFT JOIN likes l ON p.post_id = l.post_id
                            LEFT JOIN comments c ON p.post_id = c.post_id
                        WHERE p.post_id = ? AND p.is_deleted = FALSE
                        """, oneRowMapper, id).stream().findFirst();
    }

    @Override
    public List<PostList> findAll(int limit, int offset, String filter) {
        String sql;
        if (EMPTY_TAG.equals(filter)) {
            sql =
                    """
                            SELECT
                                        p.post_id,
                                        p.title,
                                        p.content,
                                        p.image_url,
                                        p.created_at,
                                        p.updated_at,
                                        p.is_deleted,
                                        l.lcount,
                                        c.ccount,
                                        tag_names.concatenated_values
                                    FROM posts p
                                        LEFT JOIN (
                                            SELECT l.post_id, COUNT(l.like_id) AS lcount
                                            FROM likes l
                                            WHERE l.is_deleted = FALSE
                                            GROUP BY l.post_id
                                        ) l ON p.post_id = l.post_id
                                        LEFT JOIN (
                                            SELECT c.post_id, COUNT(c.comment_id) AS ccount
                                            FROM comments c
                                            WHERE c.is_deleted = FALSE
                                            GROUP BY c.post_id
                                        ) c ON p.post_id = c.post_id
                                        LEFT JOIN (
                                            SELECT pt.post_id, LISTAGG(tags.name, ', ') AS concatenated_values
                                            FROM post_tags pt
                                            INNER JOIN tags ON pt.tag_id = tags.tag_id
                                            GROUP BY pt.post_id
                                        ) tag_names ON p.post_id = tag_names.post_id
                                    WHERE  p.is_deleted = FALSE
                                    ORDER BY p.created_at DESC
                                    LIMIT ? OFFSET ?
                    """;
            return jdbcTemplate.query(sql, postRowMapper, limit, offset);
        } else {
            sql =
                    """
                            SELECT
                                p.post_id,
                                p.title,
                                p.content,
                                p.image_url,
                                p.created_at,
                                p.updated_at,
                                p.is_deleted,
                                l.lcount,
                                c.ccount,
                                tag_names.concatenated_values
                            FROM posts p
                                LEFT JOIN (
                                    SELECT l.post_id, COUNT(l.like_id) AS lcount
                                    FROM likes l
                                    WHERE l.is_deleted = FALSE
                                    GROUP BY l.post_id
                                ) l ON
                                    p.post_id = l.post_id
                                LEFT JOIN (
                                    SELECT c.post_id, COUNT(c.comment_id) AS ccount
                                    FROM comments c
                                    WHERE c.is_deleted = FALSE
                                    GROUP BY c.post_id
                                ) c ON
                                    p.post_id = c.post_id 
                                INNER JOIN
                                    post_tags pt
                                ON
                                    p.post_id = pt.post_id
                                INNER JOIN tags t
                                ON
                                    pt.tag_id = t.tag_id
                                LEFT JOIN (
                                            SELECT pt.post_id, LISTAGG(tags.name, ', ') AS concatenated_values
                                            FROM post_tags pt
                                            INNER JOIN tags ON pt.tag_id = tags.tag_id
                                            GROUP BY pt.post_id
                                        ) tag_names ON p.post_id = tag_names.post_id
                                WHERE t.name = ? AND p.is_deleted = FALSE
                            ORDER BY p.created_at DESC
                            LIMIT ? OFFSET ?
                    """;
            return jdbcTemplate.query(sql, postRowMapper, filter, limit, offset);
        }


    }

//    @Override
//    public List<Post> findByTag(String tag) {
//        return jdbcTemplate.query("SELECT p.* FROM posts p INNER JOIN post_tags pt ON p.post_id = pt.post_id INNER JOIN tags t ON pt.tag_id = t.tag_id WHERE t.name = ?",
//                postRowMapper, tag);
//    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("UPDATE posts SET title = ?, content = ?, image_url = ?, updated_at = ?, is_deleted = ? WHERE post_id = ?",
                post.getTitle(), post.getContent(), post.getImageUrl(), post.getUpdatedAt(), post.isDeleted(), post.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("UPDATE posts SET is_deleted = TRUE WHERE post_id = ?", id);
    }
}
