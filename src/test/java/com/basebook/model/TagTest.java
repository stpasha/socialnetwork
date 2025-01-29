package com.basebook.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagTest {

    private static final Faker faker = new Faker();

    @Test
    public void testTagBuilder() {
        Long id = faker.number().randomNumber();
        String name = faker.lorem().word();

        Tag tag = Tag.builder()
                .id(id)
                .name(name)
                .build();

        assertThat(tag.getId()).isEqualTo(id);
        assertThat(tag.getName()).isEqualTo(name);
    }

    @Test
    public void testTagWithNullValues() {
        Tag tag = Tag.builder()
                .id(null)
                .name(null)
                .build();

        assertThat(tag.getId()).isNull();
        assertThat(tag.getName()).isNull();
    }

    @Test
    public void testToString() {
        Long id = faker.number().randomNumber();
        String name = faker.lorem().word();

        Tag tag = Tag.builder()
                .id(id)
                .name(name)
                .build();

        String tagString = tag.toString();
        assertThat(tagString).contains(id.toString());
        assertThat(tagString).contains(name);
    }

    @Test
    public void testEquals() {
        Long id = faker.number().randomNumber();
        String name = faker.lorem().word();

        Tag firstTag = Tag.builder()
                .id(id)
                .name(name)
                .build();
        Tag secondTag = Tag.builder()
                .id(id)
                .name(name)
                .build();
        assertThat(firstTag).isEqualTo(secondTag);
    }

    public static Tag createFakeTag() {
        return Tag.builder()
                .id(faker.number().randomNumber())
                .name(faker.lorem().word())
                .build();
    }

}
