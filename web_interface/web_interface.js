// TODO refactor this mess.

function populate_web_interface(){
    data_dict = JSON.parse(mwc2config);
    
    html_menu_entry = `
            <div id="entry-name-{{name}}" class="menu-entry col-12 col-md-6">
              <hr />
              <a href="#page-name-{{name}}" class="mb-0" data-title="{{fullName}}">
                <div>
                    <h4 style="margin-bottom:0.5em;">
                        <span class="fa fa-{{faIconName}}" aria-hidden="true"></span>{{fullName}}
                    </h4>
                    <small style="font-style: italic; color:#555555;">{{description}}</small>
                </div>
              </a>
            </div>`
    html_menu_page = `
            <div id="page-name-{{name}}" class="menu-page" style="display:none;" >
                <h3 style="width:100%;">
                    <a href="#" class="btn btn-light float-right" style="border: 1px solid #aaa;">
                        <span class="fa fa-arrow-left" style="padding-right:0.5em;" aria-hidden="true"></span> Back 
                        <span class="hide-collapse"> to the Control Panel</span>
                    </a>
                    <span class="fa fa-{{faIconName}} hide-collapse" aria-hidden="true"></span>{{fullName}}
                    
                </h3>
                <p style="font-style: italic; color:#555555;">{{description}}</p>   
                <hr />
                {{content}}
                {{advanced_box}}
                
                {{script}}
            </div>`
            
    html_advanced_box = `
            <button type="button" class="btn btn-light advanced-menu advanced-button" style="border: 1px solid #aaa;" 
                    data-toggle="collapse" data-target="#collapseAdvanced-{{name}}" aria-expanded="false" aria-controls="collapseAdvanced-{{name}}">
              Advanced Settings
            </button>
            <div class="collapse advanced-menu" id="collapseAdvanced-{{name}}">
              <div class="card card-body">
                {{advanced_content}}
              </div>
            </div>`
    
    html_form_field = `
            <div class="form-group row {{linkedclass}}">
                <label for="field_{{name}}" class="col-sm-3 col-form-label">{{fullName}}</label>
                <div class="col-sm-9">
                    <div class="input-group">
                    
                        {{field}}
                    
                    </div>
                    <small id="field_{{name}}_desc" class="form-text text-muted">{{description}}</small>
                </div>
              </div>`
    html_subclass_header = `
            <h5 class="subclass-header">"{{subclass}}" Settings</h5>
            <hr />
            `
    html_regular_field = `
              <input type="text" class="form-control" id="field_{{name}}" aria-describedby="field_{{name}}_desc" placeholder="{{defaultValue}}" value="{{value}}">
              <div class="input-group-append">
                  <div class="input-group-text">{{unit}}</div>
              </div>`
    html_multichoice_field = `
              <div class="btn-group btn-group-toggle" data-toggle="buttons">
                  {{choices}}
              </div>`
    html_multichoice_choice = `
              <label class="btn btn-custom {{active}}">
                <input type="radio" name="options" id="option_true" autocomplete="off" {{onfocus}} >{{choice}}
              </label>`
    

    // Change the dict into a list of elements and sort it
    var data = [];
    for (var key in data_dict) {
        data.push( { "key": key, "content": data_dict[key]} );
    }
    data = data.sort(function(a, b) {return a.content.position - b.content.position});
    console.log(data);
    
    // Create the menu entries and build the main menu
    // menu_final_html: main menu template
    var menu_final_html = "";
    for (var kk=0; kk<data.length; kk++) {
        key = data[kk].key;
        content = data[kk].content;
        
        // block_html: template of the menu entry
        block_html = html_menu_entry;
        block_html = block_html.split("{{name}}").join(key);
      
        for(var subkey in content){
            block_html = block_html.split("{{"+subkey+"}}").join(content[subkey]);
        }
        // Add the menu entry to the main menu
        menu_final_html += block_html;
    } 
    
    // Clean up for all the fields which weren't found and replace with empty string
    menu_final_html = menu_final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
    
    // Place the generated HTML into the page
    $("#menu_entries_placeholder").replaceWith( menu_final_html );
      
      
    // Create one page for each menu entry 
    // pages_final_html: block with all the hidden pages with the settings.
    var pages_final_html = "";
    for (var k=0; k<data.length; k++) {
        key = data[k].key;
        content = data[k].content;
        subclasses = data[k].content["allowedValues"];
        
        // content_html: contains all the fields that go in the page normally
        content_html = "";
        // content_advanced_ html: contains all the fields that go in the Advanced Box
        content_advanced_html = "";
        
        // If subclasses are listed, create an extra field with the allowed values.
        // Clicking on those should hide irrelevant values.
        if(subclasses !== undefined){
        
            // Create the multichoice widget with the different options
            html_choices = ""                
            for(var i=0; i<subclasses.length; i++){
                html_choices += html_multichoice_choice.split("{{choice}}").join(subclasses[i]["@type"]);
                html_choices = html_choices.split("{{onfocus}}").join( `onfocus="switchLinkedClass('`+key+`',  '`+
                                    escapeTypeName(subclasses[i]["@type"], key)+`', '`+subclasses[i]["@type"]+`');"`   );
                
                // select the active button
                if(subclasses[i]["@type"] === content.value["@type"]){
                    html_choices = html_choices.split("{{active}}").join("active");
                } else {
                    html_choices = html_choices.split("{{active}}").join("");
                }
            }
            widget_html = html_multichoice_field.split("{{choices}}").join(html_choices);
            // Inject the widget into the form field wrapper
            field_html = html_form_field.split("{{field}}").join(widget_html);

            // Fill up the resulting form field with all the metadata
            field_html = field_html.split("{{name}}").join("type-"+key);
            field_html = field_html.split("{{fullName}}").join(content.fullName+" Type");
            
            // Erase all not-found tags
            field_html = field_html.replace(/\{\{[\s\S]*?\}\}/g, "");
            
            // Add the subclass header
            field_html += html_subclass_header;
            field_html = field_html.split("{{subclass}}").join(content.value["@type"]);

            // Store the resulting field at the top of the fields listing
            content_html += field_html;
        }
          
        // Translate the fields map into a list of fields and sort them.
        var fields_list = [];
        var type = "";
        if(subclasses === undefined){
            for (var entry in content.value) {
                if(entry==="@type"){
                    type = content.value[entry];
                }
                fields_list.push( { "key": entry, "content": content.value[entry] } );
            }
        } else {
            // If there are subclasses, add the fields for all of them and label them appropriately
            console.log(subclasses);
            for (var s=0; s<subclasses.length; s++){
                for (var entry in subclasses[s]) {
                    fields_list.push( { "key": entry, "content": subclasses[s][entry], "subclass" : escapeTypeName(subclasses[s]["@type"], key) } );
                }
            }
        }
        fields_list = fields_list.sort(function(a, b) {return a.content.position - b.content.position});
        
          
        // Generate one html widget for each field
        for (var f=0; f<fields_list.length; f++) {
            field = fields_list[f].key;
            field_content = fields_list[f].content;
            field_subclass = fields_list[f].subclass;
            
            // Exclude Jackson-specific fields from rendering
            if(!field.startsWith("@")){        
                field_html = html_form_field.split("{{name}}").join(field);
                
                // Now decide how to render every field, depending on its type
                className = field_content.className;
                allowedValues = field_content.allowedValues;
                
                widget_html = "";
                // For multichoice fields we need an extra step: generating the choices
                if(allowedValues != undefined && allowedValues.length > 1){
                    html_choices = ""                
                    for(var i=0; i<allowedValues.length; i++){
                        html_choices += html_multichoice_choice.split("{{choice}}").join(allowedValues[i]);
                        
                        // select the active button
                        if(allowedValues[i] === field_content.value){
                            html_choices = html_choices.split("{{active}}").join("active");
                        } else {
                            html_choices = html_choices.split("{{active}}").join("");
                        }
                    }
                    widget_html = html_multichoice_field.split("{{choices}}").join(html_choices);
                
                } else {
                  widget_html = html_regular_field;
                }
              
                // Inject the widget into the form field wrapper
                field_html = html_form_field.split("{{field}}").join(widget_html);

                // Fill up the resulting form field with all the metadata
                for(var subfield in field_content){
                    field_html = field_html.split("{{"+subfield+"}}").join(field_content[subfield]);
                }
                field_html = field_html.split("{{linkedclass}}").join(" linkedclass-"+key+ " " +field_subclass);
                
                // Store the generated field in the regular content container, or in the Advanced Box
                if(field_content.settingType.toUpperCase() === "ADVANCED"){
                    content_advanced_html += field_html;
                } else {
                    content_html += field_html;
                }
            }
        }
        // All fields are rendered: assemble the page
        page_html = html_menu_page;
          
        // Inject rendered regular fields
        page_html = page_html.split("{{content}}").join(content_html);
        
        // Inject Advanced Box and its fields, if present.
        if(content_advanced_html !== ""){
            page_html = page_html.split("{{advanced_box}}").join(html_advanced_box);
        } else {
            page_html = page_html.split("{{advanced_box}}").join("");
        }
        
        // Inject the rest of the page metadata (see top of the loop for key and content)
        page_html = page_html.split("{{name}}").join(key);
        for(var subkey in content){
            page_html = page_html.split("{{"+subkey+"}}").join(content[subkey]);
        }
        page_html = page_html.split("{{advanced_content}}").join(content_advanced_html);
        
        // Hide the fields of all the not-relevant subclasses
        if(subclasses !== undefined){
            page_html = page_html.split("{{script}}").join(`<script>switchLinkedClass( '`+key+
                        `', '`+escapeTypeName(content.value["@type"], key) + `', '` +content.value["@type"] +`' );</script> `);
            
        }
            
        // Add the rendered page to the pile of pages  
        pages_final_html += page_html;
    }
        
    // Now clean up for all the fields which weren't found and replace with empty string
    pages_final_html = pages_final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
        
    // Replace the generated pages to the DOM
    $("#menu_pages_placeholder").replaceWith( pages_final_html );

}


function escapeTypeName(wordToEscape, scope){
    if(wordToEscape !== undefined){
        return "linkedclass-"+wordToEscape.replace(/[\W_]+/g,"").toLowerCase()+"-"+scope;
    }
}


function switchLinkedClass(scope, linkedclass, choice){
    console.log(scope, linkedclass, choice);
    // Hide fields relative to another subclass & show only the relevant ones
    $(".linkedclass-"+scope).hide();
    $("."+linkedclass).show();

    $(".subclass-header").text("\""+choice+"\" Settings");
}


function writeConfigFile(){

    alert("Save the current configuration?");
    
    // TODO Actually save the file...
    
    $('#alert-success').show();

}
