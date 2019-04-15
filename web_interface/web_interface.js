
function populate_web_interface(){

    data = JSON.parse(mwc2config);
    console.log(data);
    
    html_folding_card = `
    <div class="card">
        <div class="card-header" id="heading_{{name}}">
          <a class="mb-0" data-toggle="collapse" data-target="#collapse_{{name}}" aria-expanded="false" aria-controls="collapse_{{name}}">
                <h4 style="margin-bottom:0;"><span class="fa fa-{{faIconName}}" style="padding-right:5px;"aria-hidden="true"></span>    {{fullName}}</h4>
                <small style="font-style: italic; color:#555555;">{{description}}</small>
          </a>
        </div>
        <div id="collapse_{{name}}" class="collapse" aria-labelledby="heading_{{name}}" data-parent="#accordion">
          <div class="card-body">
          
          {{content}}
          
          </div>
        </div>
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
    
    var final_html = "";
    
    // First create the outer folding blocks
    for (var key in data) {
      block_html = html_folding_card;
      block_html = block_html.split("{{name}}").join(key);
      
      for(var subkey in data[key]){
        block_html = block_html.split("{{"+subkey+"}}").join(data[key][subkey]);
      }
      
      content_html = "";
      
      // Now add a block for each field
      for(var field in data[key]["value"]){
        
        if(!field.startsWith("@")){        
            field_html = html_form_field.split("{{name}}").join(field);
          
            // Now decide how to render every field, depending on its type
            className = data[key]["value"][field]["className"];
          
            allowedValues = data[key]["value"][field]["allowedValues"];
            console.log(field, allowedValues);
          
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
      block_html = block_html.split("{{content}}").join(content_html);
      
      final_html += block_html;
    }
    
    // Now clean up for all the fields which weren't found and replace with empty string
    final_html = final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
         
    
    
    // Replace the generated HTML into the page
    document.getElementById("accordion").innerHTML = final_html;

}




function writeConfigFile(){

    alert("Save the current configuration?");
    
    // TODO Actually save the file...
    
    $('#alert-success').show();

}
