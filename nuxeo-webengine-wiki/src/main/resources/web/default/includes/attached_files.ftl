<div id="attached_files">
    <h3>Attached files:</h3>
    <#list this.files.files as file>
    Current File: <a href="${this.docURL}@@getfile?property=files:files/item[${file_index}]/file">${file.filename}</a>
    -
    <a href="${this.docURL}@@deletefile?property=files:files/item[${file_index}]">Remove</a>
    <br/>

    </#list>
    
    <br/>

    <form id="add_file" action="${this.docURL}@@addfile" accept-charset="utf-8" method="POST" enctype="multipart/form-data">
        <label for="file_to_add">Add a new file</label>
        <input type="file" name="files:files" value="" id="file_to_add">
        <input type="submit" name="attach_file" value="Attach" id="attach_file">
    </form>
</div>