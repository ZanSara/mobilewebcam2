function navigateTo(pageName){
    $("#row-div").hide();
    $("#page-name-"+pageName).show();
}

function backToHome(){
    $('.menu-page').hide();
    $('#row-div').show();
}


function populate_web_interface(){

    data = JSON.parse(mwc2config);
    //console.log(data);
    
    html_menu_entry = `
            <div id="entry-name-{{name}}" class="menu-entry col-12 col-md-6">
              <hr />
              <a class="mb-0" onclick=navigateTo('{{name}}');>
                <h4 style="margin-bottom:0.5em;">
                    <span class="fa fa-{{faIconName}}" style="padding-right:20px;" aria-hidden="true"></span>{{fullName}}
                </h4>
                <small style="font-style: italic; color:#555555;">{{description}}</small>
              </a>
            </div>`
    
    html_menu_page = `
    <div id="page-name-{{name}}" class="menu-page" style="display:none;">
        <h3><span class="fa fa-{{faIconName}} hide-collapse" style="padding-right:0.5em;" aria-hidden="true"></span>{{fullName}}</h3>
        <p style="font-style: italic; color:#555555;">{{description}}</p>
        <hr />
        {{content}}
    </div>`
      
    html_form_field = `
                <div class="form-group row">
                    <label for="field_{{name}}" class="col-sm-3 col-form-label">{{fullName}}</label>
                    <div class="col-sm-9">
                        <div class="input-group">
                        
                            {{field}}
                        
                        </div>
                        <small id="field_{{name}}_desc" class="form-text text-muted">{{description}}</small>
                    </div>
                  </div>`
                  
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
                                <input type="radio" name="options" id="option_true" autocomplete="off">{{choice}}
                              </label>`
    
    
    // First create the enu entries
    var menu_final_html = "";
    
    // TODO Transform data into an array and then sort by position       data = data.sort(function(a, b) {return a.position - b.position});
    
    for (var key in data) {
      block_html = html_menu_entry;
      block_html = block_html.split("{{name}}").join(key);
      
      for(var subkey in data[key]){
        block_html = block_html.split("{{"+subkey+"}}").join(data[key][subkey]);
      }
      // Add it to the html
      menu_final_html += block_html;
    } 
    // Clean up for all the fields which weren't found and replace with empty string
    menu_final_html = menu_final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
    
    // Replace the generated HTML into the page
    $("#menu_entries_placeholder").replaceWith( menu_final_html );
      
      
    // Do the same, adding a page for each entry 
    var pages_final_html = "";
    for (var key in data) {
      
      page_html = html_menu_page;
      page_html = page_html.split("{{name}}").join(key);
      
      for(var subkey in data[key]){
        page_html = page_html.split("{{"+subkey+"}}").join(data[key][subkey]);
      }
     
      content_html = "";
      
      // Now add a page for each field
      for(var field in data[key]["value"]){
        
        if(!field.startsWith("@")){        
            field_html = html_form_field.split("{{name}}").join(field);
          
            // Now decide how to render every field, depending on its type
            className = data[key]["value"][field]["className"];
          
            allowedValues = data[key]["value"][field]["allowedValues"];
            //console.log(field, allowedValues);
          
            if(allowedValues != undefined && allowedValues.length > 1){
                // For multichoice fields we need an extra step: generating the choices
                html_choices = ""                
                for(var i=0; i<allowedValues.length; i++){
                    html_choices += html_multichoice_choice.split("{{choice}}").join(allowedValues[i]);
                    // select the active button
                    if(allowedValues[i]===data[key]["value"][field]["value"]){
                        html_choices = html_choices.split("{{active}}").join("active");
                    } else {
                        html_choices = html_choices.split("{{active}}").join("");
                    }
                    
                }
                widget_html = html_multichoice_field.split("{{choices}}").join(html_choices);
            
            } else {
              widget_html = html_regular_field;
            }
          
            // Inject the widget into the form field
            field_html = html_form_field.split("{{field}}").join(widget_html);

            // Fill up the resulting form field
            for(var subfield in data[key]["value"][field]){
                field_html = field_html.split("{{"+subfield+"}}").join(data[key]["value"][field][subfield]);
            }

            content_html += field_html;
        }
      }
      page_html = page_html.split("{{content}}").join(content_html);
      
      pages_final_html += page_html;
    }
    
    // Now clean up for all the fields which weren't found and replace with empty string
    pages_final_html = pages_final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
         
    
    
    // Replace the generated HTML into the page
    $("#menu_pages_placeholder").replaceWith( pages_final_html );

}




function writeConfigFile(){

    alert("Save the current configuration?");
    
    // TODO Actually save the file...
    
    $('#alert-success').show();

}
