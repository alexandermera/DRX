
var topLevelCat23Short = ["name","Agr", "Asc", "Art", "Bel", "Bus", "Chr", "Cul",
  	            			"Edu", "Env", "Geo", "Hea", "His", "Hum", "Lan",
  	          			"Law", "Lif", "Mat", "Nat", "Peo", "Pol", "Sci", "Soc", "Tec" ];

var topLevelCat23 = ["Agriculture", "Applied science", "Arts", "Belief", "Business", "Chronology", "Culture",
                     "Education", "Environment", "Geography", "Health", "History", "Humanities", "Language",
                     "Law", "Life", "Mathematics", "Nature", "People", "Politics", "Science", "Society", "Technology" ] ;

jQuery( document ).ready( function( ) 
{ 			
	jQuery('#download').focus( ) ;
	generateDendrogram( ) ;
	jQuery('#resources').DataTable( { 
	"autoWidth": true,
    paging: false,
    searching: false,
    ordering: false,
	});
	jQuery('#rankingMAP').DataTable( { 
		"autoWidth": true,
	    ordering: false,
	});
	jQuery('.intro').css('height','90%');
	jQuery('#download').css('padding-top','50px');
	jQuery("#fingerprint").click(function(){
		generateDendrogram( ) ;
	});
	//create ranking from a given dataset
	jQuery( document ).on( "click", ".recommend", function() {
		cleanTablesRanking( );
		var dsName = jQuery(this).attr("id") ;
		var idNode = jQuery(this).attr("name" ) ;
		var fileName = jQuery("#fileName").val( ) ;
		jQuery(".dsName").text(dsName);
		getRankingDatasetCatEnt( fileName, idNode, dsName ) ;
	});
	
	// Ranking 2 considering all datasets
	jQuery( document ).on( "click", ".recommend2", function() {
		cleanTablesRanking( );
		var dsName = jQuery(this).attr("id") ;
		//0.0 becase all the datasets from the root will be considered
		var idNode = "0.0" ;
		var fileName = jQuery("#fileName").val( ) ;
		jQuery(".dsName").text(dsName);
		getRankingDatasetCatEnt( fileName, idNode, dsName ) ;
	});
});



function calculateRanking( idNode ){
	cleanTables( ) ;
	cleanGraphParallel( ) ;
	
	var categoriesTh = "" ;
	for (i = 0; i < topLevelCat23.length; i++) { 
		categoriesTh += "<th align='center'>" + topLevelCat23[ i ] + " (" + topLevelCat23Short[i+1] + ")</th>" ;
	}
	
	var fileName = jQuery("#fileName").val( ) ;
	jQuery.get("/Approach/FingerPrintServlet",{request:1, fileName:fileName, idNode:idNode }, function( responseText ) 
	{
		var cont 	= 1 ;
		var json = jQuery.parseJSON( responseText ) ;
		var table = jQuery("<table id='toprank' style='font-size:11px' cellspacing='0'></table>") ;
		var data ;
		table.append( "<thead>"  +
						"<tr>" +		  
							"<th align='center'>Top</th>" +
							"<th align='center' title='Cluster membership'>Cluster</th>" +
							"<th align='center'>Dataset Name</th>" +
							"<th align='center' title='Local recommendation strategy'>Recommendation#1</th>" +
							"<th align='center' title='Global recommendation strategy'>Recommendation#2</th>" +
							categoriesTh +
						"</tr>" +
					  "</thead>" +
					"<tbody>" ) ;
		var ranking, fingerprint, csv, idclusters ;
		  
		jQuery.each( json, function( key , value ){
			if( key == 'ranking'){
				ranking = value ;
			}else if( key == 'fingerprint'){
				fingerprint = value ;
			}else if( key == 'idclusters'){
				idclusters = value ;
			}
		});
		
		jQuery.each( fingerprint, function( ds , fp ){
			var afp = new Array( ) ;
			var  row = "<tr>" +
						"<td align='center'>" + cont + "</td>" +  
						"<td align='center'>" + idclusters[ ds ] + "</td>" +  
						"<td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/" + ds + "'>" + ds +" </a></td>" +  
						"<td align='center'><span class='ui-icon-circle-triangle-e recommend' name='" + idclusters[ ds ] + "' id= '"+ds+"'>View R#1 <i class='fa fa-play-circle'></i></span></td>" +  
						"<td align='center'><span class='ui-icon-triangle-1-e recommend2' name='" + idclusters[ ds ] + "' id= '"+ds+"'>View R#2 <i class='fa fa-play-circle'></i></span></td>" ;  
			for( i=0 ; i < topLevelCat23.length ; i ++ ){
				afp.push( 0 ) ;
			}	
			jQuery.each( fp, function( cat , v ){
				afp[ topLevelCat23.indexOf( cat ) ] = v ;
			} ) ;
			for( i = 0 ; i < topLevelCat23.length ; i ++ ){
				row += "<td align='center'>" + afp[ i ].toFixed( 2 ) + "</td>" ;
			}	
			row += "</tr>" ;
			table.append( row ) ;
			cont++ ;
		} ) ;
		
		  
	  table.append( "</tbody>" +
	  '<tfoot>' +
	  '<tr>' +		  
	  '</tr>' +
	  '</tfoot>' ) ;
	  jQuery('#ranking').append( table ) ;
	  jQuery('#toprank').DataTable( { "autoWidth": true});
	  parallelCoordinate(  ) ;
	  jQuery('#result').focus( ) ;

	});
}  

function parallelCoordinate(  ){
	
	// quantitative color scale
	var blue_to_brown = d3.scale.linear()
	  .domain([9, 50])
	  .range(["steelblue", "brown"])
	  .interpolate(d3.interpolateLab);

	var color = function(d) { return blue_to_brown(d['Agr']); };

	var parcoords = d3.parcoords()("#parallel")
	    .color(color)
	    .alpha(0.4);

	// load csv file and create the chart
	d3.csv('dataset/trees/dataParallel.csv', function(data) {
	  parcoords
	    .data(data)
	    .hideAxis(["name"])
	    .composite("darker")
	    .render()
	    .commonScale()
	    .shadows()
	    .reorderable()
	    .brushMode("1D-axes");// enable brushing
	});
	var jsonData= {} ;
	var data = [ ] ;
	
	
	jQuery( "#toprank tr" ).on("mouseover",function( ) {
		  var fp = [ ] ;
	      jQuery( this ).find('td').each (function(index ) {
                 if ( index > 3 )
                       fp [ index - 4 ] = ""+ jQuery( this ).text( ) ;

         });   
		  for(i = 0 ; i < fp.length; i++){
			  jsonData[ topLevelCat23Short[ i ] ] = fp[ i ] ;
		  }
		  data.push( jsonData ) ;
		  console.log( data ) ;
		  parcoords.highlight( data ) ;
	  }).mouseout(function() {
		    parcoords.unhighlight( ) ;
	  });
}

function dendongram( fileName )
{	
	cleanGraph( ) ;
	cleanTables( ) ;
	var w = 850,
    h = 800,
    r = 650,
    x = d3.scale.linear().range([0, r]),
    y = d3.scale.linear().range([0, r]),
    node,
    root;

	var pack = d3.layout.pack()
	    .size([r, r])
	    .value(function(d) { return d.size; })
	
	var vis = d3.select("#dendogram").insert("svg:svg", "h2")
	    .attr("width", w)
	    .attr("height", h)
	  .append("svg:g")
	    .attr("transform", "translate(" + (w - r) / 2 + "," + (h - r) / 2 + ")");
	d3.json("./dataset/trees/" + fileName , function(data) {
	  node = root = data;
	  var nodes = pack.nodes(root);
	  
	vis.selectAll("circle")
      .data(nodes)
    .enter().append("svg:circle")
      .attr("class", function(d) { return d.children ? "parent" : "child"; })
      .attr("cx", function(d) { return d.x; })
      .attr("cy", function(d) { return d.y; })
      .attr("r", function(d) { return d.r; })
      .on("click", function(d) { return zoom(node == d ? root : d); });

  vis.selectAll("text")
      .data(nodes)
    .enter().append("svg:text")
      .attr("class", function(d) { return d.children ? "parent" : "child"; })
      .attr("x", function(d) { return d.x; })
      .attr("y", function(d) { return d.y; })
      .attr("dy", ".35em")
      .attr("text-anchor", "middle")
      .style("opacity", function(d) { return d.r > 20 ? 1 : 0; })
      .text(function(d) { return d.name; });

  d3.select( "#dendogram" ).on("click", function( ) { zoom( root ) ; });
});

function zoom(d, i) {
		var k = r / d.r / 2;
		x.domain([d.x - d.r, d.x + d.r]);
		y.domain([d.y - d.r, d.y + d.r]);
		var t = vis.transition()
		.duration( 3000 ) ;
		t.selectAll("circle")
		.attr("cx", function(d) { return x(d.x); })
		.attr("cy", function(d) { return y(d.y); })
		.attr("r", function(d) { return k * d.r; });
		t.selectAll("text")
		.attr("x", function(d) { return x(d.x); })
		.attr("y", function(d) { return y(d.y); })
		.style("opacity", function(d) { return k * d.r > 20 ? 1 : 0; })
		.style( "font-size" , "10px" ) ;
		node = d;
		d3.event.stopPropagation();
		calculateRanking ( d.idNode ) ;
	}
}


//list all categories & entities associated to a dataset
function getCatEnt( dsName ){
	
	jQuery.get("/Approach/FingerPrintServlet",{request:3, dsName:dsName }, function( responseText ) 
	{
		var json = jQuery.parseJSON( responseText ) ;
		var cont 	= 1 ;
		var rankingCategories ;
		var rankingEntities ;
		var table = jQuery("<table id='topRankCategories' style='font-size:11px' cellspacing='0'></table>") ;
		table.append( "<thead>"  +
				"<tr>" +		  
				"<th align='center'>N.</th>" +
				"<th align='center'>Wikipedia Category</th>" +
				"</tr>" +
				"</thead>" +
		"<tbody>" ) ;
		
		var table1 = jQuery("<table id='topRankEntities' style='font-size:11px' cellspacing='0'></table>") ;
		table1.append( "<thead>"  +
				"<tr>" +		  
				"<th align='center'>N.</th>" +
				"<th align='center'>Wikipedia Entity</th>" +
				"</tr>" +
				"</thead>" +
		"<tbody>" ) ;
		
		jQuery.each( json, function( key , value ){
			if( key == 'rankingCategories')
				rankingCategories = value ;	
			
			if( key == 'rankingEntities')
				rankingEntities = value ;			
		});
		
		jQuery.each( rankingCategories, function( i, category ){
			var  row = "<tr>" +
			"<td align='center'>" + cont + "</td>" +  
			"<td align='center'><a style='color:#000;' target='_blank' href='https://en.wikipedia.org/wiki/Category:" + category + "'>" + category +" </a></td>" ;  
			row += "</tr>" ;
			table.append( row ) ;
			cont++ ;
		} ) ;
		
		table.append( "</tbody>" +
				'<tfoot>' +
				'<tr>' +		  
				'</tr>' +
		'</tfoot>' ) ;
		
		cont = 1 ;
		
		jQuery.each( rankingEntities, function( i, entity ){
			var  row = "<tr>" +
			"<td align='center'>" + cont + "</td>" +  
			"<td align='center'><a style='color:#000;' target='_blank' href='https://en.wikipedia.org/wiki/" + entity + "'>" + entity +" </a></td>" ;  
			row += "</tr>" ;
			table1.append( row ) ;
			cont++ ;
		} ) ;
		
		table1.append( "</tbody>" +
				'<tfoot>' +
				'<tr>' +		  
				'</tr>' +
		'</tfoot>' ) ;
		
		jQuery('#rankingCategories').append( table ) ;
		jQuery('#rankingEntities').append( table1 ) ;
		jQuery('#topRankCategories').DataTable( { "autoWidth": false});
		jQuery('#topRankEntities').DataTable( { "autoWidth": false});
	});
}

function getRankingDatasetCatEnt( fileName, idNode, dsName) {
	jQuery.get("/Approach/FingerPrintServlet",{request:2, fileName:fileName, idNode:idNode, dsName:dsName }, function( responseText ) 
			{
				var cont 	= 1 ;
				var json = jQuery.parseJSON( responseText ) ;
				var table = jQuery("<table id='topRankCluster' style='font-size:11px' cellspacing='0'></table>") ;
				var data ;
				table.append( "<thead>"  +
								"<tr>" +		  
									"<th align='center'>Top</th>" +
									"<th align='center'>Cluster</th>" +
									"<th align='center'>Dataset Name</th>" +
									"<th align='center'>Distance</th>" +
									"<th align='center'>Gold standard</th>" +
								"</tr>" +
							  "</thead>" +
							"<tbody>" ) ;
				var ranking, idclusters, gs;
				  
				jQuery.each( json, function( key , value ){
					if( key == 'ranking')
						ranking = value ;			
					if( key == 'idclusters')
						idclusters = value ;			
					if( key == 'gs')
						gs = value ;			
				});
				
				jQuery.each( ranking, function( ds , score ){
					var  row = "<tr>" +
								"<td align='center'>" + cont + "</td>" +  
								"<td align='center'>" + idclusters[ ds ] + "</td>" +  
								"<td align='center'><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/" + ds + "'>" + ds +" </a></td>" +  
								"<td align='center'>" + score.toFixed( 3 )  + "</td>" +
								"<td align='center'>" + gs[ ds ] + "</td>" ; 
					row += "</tr>" ;
					table.append( row ) ;
					cont++ ;
				} ) ;
				
			  table.append( "</tbody>" +
			  '<tfoot>' +
			  '<tr>' +		  
			  '</tr>' +
			  '</tfoot>' ) ;
			  jQuery('#rankingCluster').append( table ) ;
			  jQuery('#topRankCluster').DataTable( { "autoWidth": false});
			  jQuery('#resultCluster').focus( ) ;
			});
	getCatEnt( dsName ) ;
}

function cleanGraph( )
{
	  jQuery("#dendogram").empty( ) ;
}
function cleanGraphParallel( )
{
	jQuery("#parallel").empty( ) ;
}

function GetURLParameter( sParam )
{
    var sPageURL = window.location.search.substring( 1 ) ;
    var sURLVariables = sPageURL.split( '&' ) ;
    for (var i = 0; i < sURLVariables.length; i++ ) 
    {
        var sParameterName  = sURLVariables[i].split('=') ;
        
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}

function disableControls ( p )
{
	 jQuery("#fileChooser").attr("disabled", p ) ;
	 jQuery("#lang").attr("disabled", p ) ;
	 jQuery("#submit").attr("disabled", p ) ;
	 jQuery("#uncover").attr("disabled", !p) ;
}

function cleanTables( )
{
	  jQuery("#ranking").empty( ) ;
	  jQuery("#parallel").empty( ) ;
	  cleanTablesRanking( ) ;
}
function cleanTablesRanking( )
{
	jQuery("#rankingCluster").empty( ) ;
	jQuery("#rankingCategories").empty( ) ;
	jQuery("#rankingEntities").empty( ) ;
	jQuery(".dsName").text("");
}
function cleanTablesCategoriesEntities( )
{
	jQuery("#rankingCategories").empty( ) ;
	jQuery("#rankingEntities").empty( ) ;
	jQuery(".dsName").text("");
}

function validate( )
{
	var isfile = jQuery("#fileChooser").val( ) ;
	
	
	if( isfile.length <= 0  )
	{
		print("Select a file first!") ;
		return  false ;
	}
	else
	{
		return true ;
	}
}

function generateDendrogram( )
{
	var nminc = jQuery("#nminc").val( ) ;
	var nmaxc = jQuery("#nmaxc").val( ) ;
	var seed = jQuery("#seed").val( ) ;
	jQuery("#status_indicator").addClass("loading") ;
	jQuery("#nminc").attr("disabled", true) ;
	jQuery("#nmaxc").attr("disabled", true) ;
	jQuery("#seed").attr("disabled", true) ;
	jQuery.get("/Approach/FingerPrintServlet",{request:0, nminc:nminc, nmaxc:nmaxc, seed:seed},function( responseText ) 
	{
		var json		= jQuery.parseJSON( responseText ) ;
		var status  	= false ;
		var fileName 	= "" ;
		jQuery("#status_indicator").removeClass("loading") ;
		jQuery("#nminc").attr("disabled", false) ;
		jQuery("#nmaxc").attr("disabled", false) ;
		jQuery("#seed").attr("disabled", false) ;
		
		jQuery.each( json, function( key , value )
		{	
			if( key == 'fileName'){
				fileName = value ;
			}
			else if( key == 'status'){
				status = value ;
			}
		});
		if( status ){
			dendongram( fileName ) ;
			jQuery('#fileName').val( fileName ) ;
			jQuery('#download').focus( ) ;
		}
		
	});
}

jQuery (function() 
 {
    jQuery("input:file").change(function ( )
    {
      var fn = jQuery( this ).val( ) ;
      jQuery( "#infoFile" ).html( fn ) ;
    });
});