package com.example.harumeonglog.restDocs.base;

import com.example.harumeonglog.domain.common.config.data.ProfileConfigData;
import com.example.harumeonglog.domain.common.util.discord.service.DiscordService;
import com.example.harumeonglog.restDocs.config.RestDocsTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

@Import({RestDocsTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public abstract class AbstractRestDocsTest {

    protected static final String IDENTIFIER = "{class-name}/{method-name}";
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    protected final ResponseFieldsSnippet commonResponse = responseFields(
            fieldWithPath("isSuccess").description("성공 여부"),
            fieldWithPath("code").description("상태 코드"),
            fieldWithPath("message").description("상태 코드에 따른 메시지"),
            subsectionWithPath("result").description("결과 데이터")
    );

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    // ExceptionAdvice Bean
    @MockitoBean
    DiscordService discordService;

    @MockitoBean
    ProfileConfigData profileConfigData;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(final WebApplicationContext context,
               final RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected final Attributes.Attribute defaultValue(String value) {
        return key("defaultValue").value(value);
    }

    protected final Attributes.Attribute required(boolean required) {
        return key("required").value(String.valueOf(required));
    }
}
