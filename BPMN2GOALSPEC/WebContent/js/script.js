
function popupCredits(url, title, w, h) {
var left = (screen.width/2)-(w/2);
var top = (screen.height/2)-(h/2);
return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
} 

function popupDialog() {
	 $( "#dialog" ).dialog({
		 width: 400
	 });
}

function clickFile(){

	document.getElementById('my_file').click();
}

function readLocalFile(fileName){
	console.log("FILE NAME-->"+fileName);
jQuery.get(fileName, function(data) { 
		
		callServicegetGoalsSpec(data);
	 });
}

function callServicegetGoalsSpec(xmlString){
	var ipAdress="aose.pa.icar.cnr.it:8080";
	
	$('#bpmnTextArea').val(xmlString)
	//var ipAdress="localhost:8080";
	 $.ajax({
		url : "http://"+ipAdress+"/BPMN2REQWEB/GetGoalSpecFromBPMNServlet",
	    type: "POST",
		data:{"bpmnDiagramm":xmlString},
	    crossDomain: true,
	    dataType: 'json',
	    success : function (data) {
	    	//PER STAMPARE I GOALS A VIDEO IN UNA TEXT AREA
	    	
	    	$('#goalsTextArea').val(data.goals)
	    	
	    	
//	    	$('#goalSPecTab').className += " active";
	    	openTab(event, 'GOALSPEC');
	    	document.getElementById("goalSPecTab").className +=  " active";
	    	//PER SCARICARE IL FILE CONTENENT I GOALS
//	    	var textFileAsBlob = new Blob([data.goals], {type:'text/xml'});
//			var fileNameToSaveAs = "GoalSPECToBPMN";
//
//			var downloadLink = document.createElement("a");
//			downloadLink.download = fileNameToSaveAs;
//			downloadLink.innerHTML = "Download File";
//			if (window.webkitURL != null)
//			{
//				downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
//			}
//			else
//			{
//				
//				downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
//				downloadLink.style.display = "none";
//				document.body.appendChild(downloadLink);
//			}
//
//			downloadLink.click();
			},
			
	     error : function (richiesta,stato,errori) {
	    	 alert("ERROR:NOT VALID FILE!")
	   	    }
	   
	}); 
}
function uploadXMIFile(){
	
	 $('#customfileupload').html( $('#my_file').val());
	  
	 var file_input=document.getElementById("my_file").files[0];
	 var fileReader = new FileReader();
	
	 var xmlString =""
	
		fileReader.onload = function(fileLoadedEvent) 
		{
			xmlString = fileLoadedEvent.target.result;
			callServicegetGoalsSpec(xmlString);
			 
		}
		fileReader.readAsText(file_input, "UTF-8");
		
}


function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
  
    evt.currentTarget.className += " active";
}