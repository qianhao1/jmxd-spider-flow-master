package org.spiderflow.core.executor.function.extension;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.annotation.Return;
import org.spiderflow.core.utils.ExtractUtils;
import org.spiderflow.executor.FunctionExtension;
import org.spiderflow.io.SpiderResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseFunctionExtension implements FunctionExtension{

	@Override
	public Class<?> support() {
		return SpiderResponse.class;
	}
	
	@Comment("将请求结果转为Element对象")
	@Example("${resp.element()}")
	public static Element element(SpiderResponse response){
		return Jsoup.parse(response.getHtml());
	}
	
	@Comment("根据xpath在请求结果中查找")
	@Example("${resp.xpath('//title/text()')}")
	@Return({Element.class,String.class})
	public static Object xpath(SpiderResponse response,String xpath){
		return ExtractUtils.getObjectValueByXPath(element(response), xpath);
	}
	
	@Comment("根据xpath在请求结果中查找")
	@Example("${resp.xpaths('//a/@href')}")
	public static List<Object> xpaths(SpiderResponse response,String xpath){
		return ExtractUtils.getObjectValuesByXPath(element(response), xpath);
	}
	
	@Comment("根据正则表达式提取请求结果中的内容")
	@Example("${resp.regx('<title>(.*?)</title>')}")
	public static String regx(SpiderResponse response,String pattern){
		return ExtractUtils.getFirstMatcher(response.getHtml(), pattern, true);
	}
	
	@Comment("根据正则表达式提取请求结果中的内容")
	@Example("${resp.regxs('<h2>(.*?)</h2>')}")
	public static List<String> regxs(SpiderResponse response,String pattern){
		return ExtractUtils.getMatchers(response.getHtml(), pattern, true);
	}
	
	@Comment("根据css选择器提取请求结果")
	@Example("${resp.selector('div > a')}")
	public static Element selector(SpiderResponse response,String selector){
		return ElementFunctionExtension.selector(element(response), selector);
	}
	
	@Comment("根据css选择器提取请求结果")
	@Example("${resp.selectors('div > a')}")
	public static Elements selectors(SpiderResponse response,String selector){
		return ElementFunctionExtension.selectors(element(response), selector);
	}
	
	@Comment("根据jsonpath提取请求结果")
	@Example("${resp.jsonpath('$.code')}")
	public static Object jsonpath(SpiderResponse response,String path){
		return ExtractUtils.getValueByJsonPath(response.getJson(), path);
	}

}
