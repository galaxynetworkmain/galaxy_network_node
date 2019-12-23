package org.gn.blockchain.shell.config;

import org.gn.blockchain.config.CommonConfig;
import org.gn.blockchain.config.NoAutoscan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Override default blockchain config to apply custom configuration.
 * This is entry point for starting EthereumJ core beans.
 */
@Configuration
@ComponentScan(
        basePackages = "org.gn.blockchain",
        excludeFilters = @ComponentScan.Filter(NoAutoscan.class))
public class EthereumHarmonyConfig extends CommonConfig {
}
