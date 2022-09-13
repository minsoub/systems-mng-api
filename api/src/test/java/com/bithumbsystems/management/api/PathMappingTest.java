package com.bithumbsystems.management.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

class PathMappingTest {

  @Test
  void test() {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    assertTrue(pathMatcher.match("/api/v1/site/list", "/api/v1/site/list"));
    assertTrue(pathMatcher.match("/a/{b}/c/{ddd}", "/a/1/c/2"));
    assertFalse(pathMatcher.match("/a/{b}/c/{ddd}", "/a/1/2"));
    assertFalse(pathMatcher.match("/a/c/{ddd}", "/a/1/c/2"));
    assertTrue(pathMatcher.match("/a/c/{ddd}", "/a/c/test"));
    assertTrue(pathMatcher.match("/a/{b}/c", "/a/test/c"));
    assertFalse(pathMatcher.match("/a/{b}/c/d", "/a/test/c"));
  }

  @Test
  void isValidPassword() {
    String password = "Test1233##";
    String wrongPassword = "123";

    var regex = "^.*(?=^.{8,64}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^*]).*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher trueMatcher = pattern.matcher(password);
    Matcher falseMatcher = pattern.matcher(wrongPassword);

    assertTrue(trueMatcher.matches());
    assertFalse(falseMatcher.matches());

  }

}
