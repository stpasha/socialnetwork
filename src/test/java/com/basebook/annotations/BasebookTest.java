package com.basebook.annotations;

import com.basebook.BasebookApplication;
import com.basebook.test.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = BasebookApplication.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
public @interface BasebookTest {
}
