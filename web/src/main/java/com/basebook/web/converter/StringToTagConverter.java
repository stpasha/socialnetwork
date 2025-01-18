package com.basebook.web.converter;

import com.basebook.model.Tag;
import com.basebook.service.TagService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTagConverter implements Converter<String, Tag> {

    private final TagService tagService;

    public StringToTagConverter(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public Tag convert(String source) {
        try {
            Long tagId = Long.valueOf(source); // Преобразуем строку в Long
            return tagService.get(tagId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid tag ID format: " + source, e);
        }
    }
}
