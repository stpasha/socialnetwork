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
            Long tagId = Long.valueOf(source);
            return tagService.get(tagId)
                    .orElseThrow(() -> new IllegalArgumentException("Malicious request for tag"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid tag ID format: " + source, e);
        }
    }
}
