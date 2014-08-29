package com.softserveinc.ita.kaiji;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ContextConfiguration.class, SecurityConfiguration.class})
public class TestServiceConfiguration {}
