<@extends src="/default/base.ftl">
<@block name="content">
<h2>Edit Wiki Page : ${This.title} </h2>
<form method="POST" action="${This.urlPath}@@update" accept-charset="utf-8">
<textarea name="note:note" cols="80" rows="20">${This.note.note}</textarea>
<p>
Title: <input type="text" name="dc:title" value="${Document.dublincore.title}"/>
</p>
<p>
Description: <input type="text" name="dc:description" value="${Document.dublincore.description}"/>
</p>
<input type="submit"/>
</form>
</@block>
</@extends>