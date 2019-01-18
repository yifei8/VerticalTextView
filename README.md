# VerticalTextView

## Demo
![demo.gif](https://upload-images.jianshu.io/upload_images/2116778-b9a358c60a1594c8.gif?imageMogr2/thumbnail/!50p)

##
```
implementation "com.sjtu.yifei:vertical-textview:1.4.1"
```

## 最新版本
[ ![Download](https://api.bintray.com/packages/iyifei/maven/vertical-textview/images/download.svg?version=1.4.1) ](https://bintray.com/iyifei/maven/vertical-textview/1.4.1/link)

## 参数说明
参数|类型|说明|
---|---|---|
text|string&reference|文本|
textColor|color&reference|字体颜色|
textSize|dimension&reference|字体大小|
rowSpacing|dimension&reference|行间距|
columnSpacing|dimension&reference|列间距|
columnLength|integer|一列文字的长度，建议使用该属性来限制高度|
maxColumns|integer|最大限制的列数，超出的部分使用纵向省略号|
atMostHeight|boolean|默认true，使用确定高度值时，会对纵向尾部不够文字多出的空白会自动去掉，使用时如果强制显示这种空白需要设置为false|
isCharCenter|boolean|默认true，true表示文字使用Paint.Align.CENTER ，false表示文字使用Paint.Align.LEFT |
textStyle|枚举0/1/2|normal/bold/italic|


