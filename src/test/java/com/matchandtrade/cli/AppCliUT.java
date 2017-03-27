package com.matchandtrade.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AppConfiguration;
import com.matchandtrade.config.AppConfigurationProperties;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AppCliUT {
	
	@Autowired
	AppConfiguration appConfiguration;
	
	@Test(expected=IOException.class)
	public void configFileNegativeDirectory() throws IOException {
		String directoryPath = AppCliUT.class.getProtectionDomain().getCodeSource().getLocation().getPath();		
		String[] arguments = {"-cf", directoryPath};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test
	public void configFilePositive() throws IOException {
		
		String filePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "matchandtrade.properties";
		String[] arguments = {"-cf", filePath};
		new AppCli(arguments);
		assertEquals(filePath, System.getProperty(AppConfigurationProperties.Keys.CONFIG_FILE.getKey()));
	}

	
	@Test(expected=IOException.class)
	public void configFileNegativeFileNotFound() throws IOException {
		String[] arguments = {"-cf", "configFileTest"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test(expected=InvalidParameterException.class)
	public void configFileNegativeMissingArgument() throws IOException {
		String[] arguments = {"--configFile"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test
	public void help() throws IOException {
		String[] arguments = {"-h"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
		assertTrue(cli.getCommandLineOutputMessage().length()>0);
	}

	@Test
	public void empty() throws IOException {
		String[] arguments = {""};
		AppCli cli = new AppCli(arguments);
		assertEquals(false, cli.isInterrupted());
	}

}