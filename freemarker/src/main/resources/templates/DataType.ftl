<#--
	数据类型：布尔类型
        布尔类型不能直接输出，如果输出要先转成字符串
	    方式一：?c
	    方式二：?string 或 ?string("true时的文本","false时的文本")
-->
1. 布尔类型:
用法1：${boolean_flag?c}
用法2：${boolean_flag?string}
用法3：${boolean_flag?string("Y","N")}
用法4：${boolean_flag?string("是","否")}
用法5：${boolean_flag?then('Y', 'N')}


<#--
	数据类型：日期类型
		日期类型不能直接输出，如果输出要先转成日期型或字符串
		用法1 年月日 ?date
		用法2 时分秒 ?time
		用法3 年月日时分秒 ?datetime
		用法4 制定格式 ?string("自定义格式") y:年 M:月 d:日 H:时 m:分 s:秒
-->

2. 日期类型
<#--输出日期格式 -->
用法1：日期格式：${date?date}
<#--输出时间格式 -->
用法2：时间格式：${date?time}
<#--输出日期时间格式 -->
用法3：日期时间格式：${date?datetime}
<#--输出格式化日期格式 -->
用法4：自定义格式：${date?string("yyyy-MM-dd HH:mm:ss")}
<#-- 字符串输出日期时间格式 -->
用法5：日期时间格式：${dateStr?datetime}



<#--
	数据类型：数值类型
		数值类型可以直接输出；
		1. 转字符串
			普通字符串     ?c
			货币字符串     ?string.currency
			百分比型字符串  ?string.percent
		2. 保留浮点型数值指定小数位（#表示一个小数位）
			?string["0.##"]
-->

3. 数值类型

<#-- 直接输出数值类型-->
用法1：${age}
用法2：${salary}
用法3：${avg}
<#-- 将数值转换成字符串输出-->
用法4：${salary?c}
<#--将数值转换成货币类型的字符串输出 -->
用法5：${salary?string.currency}
<#--将数值转换为百分比的字符串-->
用法6：${avg?string.percent}
<#--将浮点型数值保留指定小数位输出（##表示保留两位小数）-->
用法7：${avg?string["0.##"]}




<#--
	数据类型：字符串类型
		在freemarker中字符串类型可以直接输出；
		1，截取字符串（坐闭右开）	?substring(start,end)
		2,首字母小写输出 	  ?uncap_first
		3，首字母大写输出 	 ?cap_first
		4，字母转小写输出 	 ?lower_case
		5，字母转大写输出	 ?upper_case
		6，获取字符串长度 	 ?length
		7，是否以指定字符串开头(boolean类型)     ?starts_with("xx")?string
		8，是否以指定字符结尾(boolean类型)		?ends_with("xx")?string
		9，获取指定字符的索引 	?index_of("xx")
		10，去除字符串前后空格  	?trim
		11，替换指定字符串   ?replace("xx","xx")

-->
4. 字符串类型
<#-- 字符串直接输出 -->
用法1：${str}
用法2：${str?string}
<#-- 截取字符串（坐闭右开）	?substring(start,end) -->
用法2：${str?substring(0,3)}
<#-- 首字母小写输出 ?uncap_first -->
用法2：${str?uncap_first}
<#-- 首字母大写输出 ?cap_first -->
用法2：${str?cap_first}
<#-- 字母转小写输出 ?lower_case -->
用法2：${str?lower_case}
<#-- 字母转大写输出 ?upper_case -->
用法2：${str?upper_case}
<#-- 获取字符串长度 ?length -->
用法2：${str?length}
<#-- 是否以指定字符串开头(boolean类型)     ?starts_with("xx")?string -->
用法2：${str?starts_with("a")?string}
<#-- 是否以指定字符结尾(boolean类型)		?ends_with("xx")?string -->
用法2：${str?ends_with("o")?string}
<#-- 获取指定字符的索引 	?index_of("xx") -->
用法2：${str?index_of("m")}
<#-- 去除字符串前后空格  	?trim -->
用法2：${str?trim}
<#-- 替换指定字符串   ?replace("xx","xx") -->
用法2：${str?replace("he","we")}