<%@page contentType="text/html; charset=UTF-8" language="java" import="java.util.*" %>
<html>
<body>
<h2>你好啊</h2>
<form action="/manage/product/uplodeFile.do" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit">
</form>

<form name="form2" action="/manage/product/richTextImgUpload.do" method="post"  enctype="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="upload"/>
</form>
</body>
</html>
