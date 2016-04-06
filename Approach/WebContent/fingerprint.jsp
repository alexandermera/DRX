<?xml version="1.0" encoding="ISO-8859-1" ?>  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br">
<head>
	<link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
	<script type="text/javascript" charset="utf8" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" charset="utf8" src="http://cdn.datatables.net/1.10.7/js/jquery.dataTables.js"></script>
	<link rel="stylesheet" href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/d3.parcoords.css" media="all" />
  	
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.ui.effects.js"></script>  
    <script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery-ui.min.js"></script>    
    <script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.form.js"></script>    
    <link rel="stylesheet" href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/jquery-ui.css" media="all" />
	<link rel="stylesheet" href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/estilos.css" media="all" />
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>library/jquery.cookie.js" ></script>
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/d4.js"></script>
 	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/d3.svg.multibrush.js"></script>
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/d3.parcoords.js"></script>
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>fingerprintServlet.js"></script>
	
    <!-- Bootstrap Core CSS -->
    <link href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Custom CSS -->
    <link href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/grayscale.css" rel="stylesheet"/>

    <!-- Custom Fonts -->
    <link href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="http://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic" rel="stylesheet" type="text/css"/>
    <link href="http://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css"/>
    <title>Linked Data Analysis</title>
</head>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">
    <!-- Navigation -->
    <nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-main-collapse">
                    <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand page-scroll" href="#page-top">
                    <i class="fa fa-play-circle"></i>  <span class="light">DRX: A LOD dataset recommendation and visualization tool</span>
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-right navbar-main-collapse">
                <ul class="nav navbar-nav">
                    <!-- Hidden li included to remove active class from about link when scrolled up past about section -->
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li>
                        <a class="page-scroll" href="register.jsp">Add a New Dataset</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#experiment">Experiments</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#source">Download</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="faq.jsp">FaQ</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#about">About</a>
                    </li>
                </ul>
                
            </div>
            
            <div class="collapse navbar-collapse navbar-right navbar-main-collapse">
                <ul class="nav navbar-nav">
                    <!-- Hidden li included to remove active class from about link when scrolled up past about section -->
                    <li>
                    
                    </li>
                    <li>
                        #MinClusters: <input title= "Minimum number of cluster" style="text-align: center" type="number" min="2" max="100" step="1" value="13"id="nminc"/>
                    </li>
                    <li>
                        #MaxClusters: <input title= "Maximum number of cluster" style="text-align: center" type="number" min="2" max="100" step="1" value="13" id="nmaxc"/>
                    </li>
                    <li>
                       Seed: <input title= "Number of seeds"  style="text-align: center" type="number" min="1" max="100" step="1" value="10" id="seed"/>
                    </li>
                    <li>
                       <input type="button" style="line-height:21px" value="Regenerate Clusters" id="fingerprint" title="Set the parameters Min, Max and Seed to generate a new clusterization of the datasets. The default parameters are: Min: 13, Max: 13 and Seed: 10."></input>
                    </li>
                    
                    <li>
                        <div id="status_indicator">&nbsp;</div>
                    </li>
                </ul>
                
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Intro Header 
    <header class="intro">
        <div class="intro-body">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                    	<p style="line-height: 120%;">DRX uses the X-Mean Cluster algorithm for clustering fingerprints of the datasets.
                    	Grouping datasets through their fingerprints allows a better exploration of the LOD cloud.
                    	If a cluster is selected a table displaying the dataset members will be shown.</p>
						<fieldset>
  							<legend>X-Means Clusters based on Top-Level Categories:</legend>
	  						NumMinClusters: <input title= "Minimum number of cluster" style="text-align: center" type="number" min="2" max="100" step="1" value="13"id="nminc"/>
	  						NumMaxClusters: <input title= "Maximum number of cluster" style="text-align: center" type="number" min="2" max="100" step="1" value="13" id="nmaxc"/>
	  						Seed: <input title= "Number of seeds"  style="text-align: center" type="number" min="1" max="100" step="1" value="10" id="seed"/>
	  						<input type="button" value="Calculate Clusters" id="fingerprint"></input>
	  						<div id="status_indicator">&nbsp;</div>
						</fieldset>
						<input type="hidden" id="fileName"></input>
                    </div>
                </div>
            </div>
        </div>
    </header>-->
    
 <!-- Download Section -->
    <section id="download" tabindex="-1" class="container content-section text-center">
    	<div class="row">
        	<div class="download-section">
                <input type="hidden" id="fileName"></input>
                  <h4 style="position:relative;top:60px">Dataset Clusters (Dendrogram):</h4>
                  <p style="position:relative;top:23px; font-size:11px">Select a cluster and scroll down to view more detailed cluster information.</p>
                  <div id="dendogram"></div>
            </div>
        </div>
    </section>
    
 <!-- Result Section -->
    <section id="result" class="container content-section text-center">
			<h4>Dataset Profiling Representation (Fingerprint):</h4>
			<p style="position:relative;top:-35px;font-size:11px">(1) Move the mouse over any row in the table to view a graphic representation of the fingerprint for a specific dataset.
			<br/>(2) Use the search field to find a dataset within the cluster selected above.
			<br/>(3) Click on the recommendation strategy (R#1 or R#2) of the dataset of interest to obtain a ranked list of related datasets.
			<br/>(4) Scroll down to see the list of related datasets for the previously selected dataset.
			<br/>The R#1 recommendation strategy considers the datasets belonging to the selected cluster whereas the R#2 takes into account all datasets from all clusters.
			</p>
			<p></p>
			<div class="row">
				<div id="parallel" class="parcoords" style="height:200px;position: relative;right: 580px;"></div>
				<div id="ranking" style="overflow:auto;"></div>
    		</div>                  
   	</section>
    <section id="resultCluster" class="container content-section text-center">
            <h4>Recommendations for: <span class="dsName"></span></h4>
            <p style="position:relative;top:-35px; font-size:11px">List of recommendations for <span class="dsName"></span> ordered by the cosine distance.<br/>Rankings with distances closer to zero are more similar, while those with distances closer to 1 are more different.</p>
			<div class="row">
				<div id="rankingCluster" style="width:700px;position:relative;left:250px;"></div>
    		</div>                  
   	</section>
    <section id="resultCategories" class="container content-section text-center">
            <h4>Wikipedia Categories for: <span class="dsName"></span></h4>
            <p style="position:relative;top:-35px; font-size:11px">List of Wikipedia categories for <span class="dsName"></span>.</span></p>
			<div class="row">
				<div id="rankingCategories" style="width:700px;position:relative;left:250px;"></div>
    		</div>                  
   	</section>
    <section id="resultEntities" class="container content-section text-center">
            <h4>Wikipedia Entities for: <span class="dsName"></span></h4>
            <p style="position:relative;top:-35px; font-size:11px">List of Wikipedia entities for <span class="dsName"></span> extracted using Wikipedia Miner.</span></p>
			<div class="row">
				<div id="rankingEntities" style="width:700px;position:relative;left:250px;"></div>
    		</div>                  
   	</section>
   	
   	    <!-- Resources Section -->
    <section id="experiment" class="container content-section text-center">
    	<h4>Experiments:</h4>
           <div class="row">
            <div class="col-lg-8 col-lg-offset-2">
                <h4>Global-based strategy recommendation:</h4>
                <span>This table shows the results after applying the Global-based strategy during the experiments. Datasets are grouped by their MAP values.</span>
				<table id='rankingMAP' style='font-size:9px'>
					<thead>
						<tr>
							<th align='center'>0-20%</th>
							<th align='center'>21-50%</th>
							<th align='center'>51-100%</th>
						</tr>	
					</thead>
					<tbody>
						<tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/the-eurostat-linked-data'>the-eurostat-linked-data </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-rae2001'>eea </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-wiki'>shoah-victims-names </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/southampton-ecs-eprints'>southampton-ecs-eprints </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-lisbon'>rkb-explorer-darmstadt </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-budapest'>rkb-explorer-roma </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/thist'>thist </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-lebsanft-org'>oecd-linked-data </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-newcastle'>rkb-explorer-laas </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statistics-data-gov-uk'>statistics-data-gov-uk </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-irefindex'>bio2rdf-iproclass </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-eprints'>rkb-explorer-pisa </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/kupkb'>kupkb </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bis-linked-data'>bio2rdf-omim </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/sudocfr'>rkb-explorer-irit </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-era'>rkb-explorer-era </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/reload'>bio2rdf-affymetrix </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-kisti'>imf-linked-data </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/pokepedia-fr'>pokepedia-fr </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/eprints-harvest-rkbexplorer'>environment-agency-bathing-water-quality </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/idreffr'>rkb-explorer-citeseer </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/muninn-world-war-i'>muninn-world-war-i </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-orphanet'>rkb-explorer-ft </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/frb-linked-data'>rkb-explorer-southampton </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-ja'>dbpedia-ja </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-dbsnp'>eea-rod </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-ieee'>statusnet-skilledtests-com </a></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dutch-ships-and-sailors'>dutch-ships-and-sailors </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/abs-linked-data'>bio2rdf-ctd </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/lista-encabezamientos-materia'>lista-encabezamientos-materia </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-resex'>bio2rdf-hgnc </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-clinicaltrials'>bio2rdf-clinicaltrials </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-deploy'>world-bank-linked-data </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/ocd'>ocd </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-ibm'>uis-linked-data </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/open-data-thesaurus'>open-data-thesaurus </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-os'>rkb-explorer-curriculum </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/linkedgeodata'>linkedgeodata </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-italy'>bio2rdf-biomodels </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/nobelprizes'>nobelprizes </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/ecb-linked-data'>rkb-explorer-ulm </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-doomicile-de'>statusnet-doomicile-de </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-postblue-info'>bio2rdf-drugbank </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-risks'>rkb-explorer-risks </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/eionet-rdf'>rkb-explorer-nsf </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/education-data-gov-uk'>education-data-gov-uk </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-planet-libre-org'>rkb-explorer-kaunas </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/serendipity'>serendipity </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-sgd'>rkb-explorer-courseware </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-recit-org'>statusnet-recit-org </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-homologene'>rkb-explorer-eurecom </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/gene-expression-atlas-rdf'>gene-expression-atlas-rdf </a></td><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-cordis'>morelab </a></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/acorn-sat'>acorn-sat </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/hellenic-fire-brigade'>hellenic-fire-brigade </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/semantic-web-dog-food'>semantic-web-dog-food </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/epo'>epo </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-kenzoid-com'>statusnet-kenzoid-com </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/chembl-rdf'>chembl-rdf </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/openmobilenetwork'>openmobilenetwork </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-qth-fr'>statusnet-qth-fr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/opendataec'>opendataec </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-tschlotfeldt-de'>statusnet-tschlotfeldt-de </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/utpl-lod'>utpl-lod </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/nextweb-gnoss'>nextweb-gnoss </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/linklion'>linklion </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/geological-survey-of-austria-thesaurus'>geological-survey-of-austria-thesaurus </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/courts-thesaurus'>courts-thesaurus </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-live'>dbpedia-live </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/open-energy-info-wiki'>open-energy-info-wiki </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/the-drug-interaction-knowledge-base'>the-drug-interaction-knowledge-base </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/disgenet'>disgenet </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-wormbase'>bio2rdf-wormbase </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbtropes'>dbtropes </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/europeana-lod'>europeana-lod </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rdflicense'>rdflicense </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/educationalprograms_sisvu'>educationalprograms_sisvu </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/archiveshub-linkeddata'>archiveshub-linkeddata </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/socialsemweb-thesaurus'>socialsemweb-thesaurus </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-webscience'>rkb-explorer-webscience </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bluk-bnb'>bluk-bnb </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/geolinkeddata'>geolinkeddata </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/transport-data-gov-uk'>transport-data-gov-uk </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/l3s-dblp'>l3s-dblp </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-ncbigene'>bio2rdf-ncbigene </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/fao-linked-data'>fao-linked-data </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dnb-gemeinsame-normdatei'>dnb-gemeinsame-normdatei </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/tags2con-delicious'>tags2con-delicious </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/amsterdam-museum-as-edm-lod'>amsterdam-museum-as-edm-lod </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/biosamples-rdf'>biosamples-rdf </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/esd-toolkit'>esd-toolkit </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bfs-linked-data'>bfs-linked-data </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/zdb'>zdb </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-1w6-org'>statusnet-1w6-org </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-pt'>dbpedia-pt </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-eu'>dbpedia-eu </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/asn-us'>asn-us </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/stw-thesaurus-for-economics'>stw-thesaurus-for-economics </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/icane'>icane </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/productontology'>productontology </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/artenuevosmedios-gnoss'>artenuevosmedios-gnoss </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-jisc'>rkb-explorer-jisc </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-dotac'>rkb-explorer-dotac </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/linkedmdb'>linkedmdb </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-oai'>rkb-explorer-oai </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-el'>dbpedia-el </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/datos-bne-es'>datos-bne-es </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-fr'>dbpedia-fr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-kathryl-fr'>statusnet-kathryl-fr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/flickr-wrappr'>flickr-wrappr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/reference-data-gov-uk'>reference-data-gov-uk </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-nl'>dbpedia-nl </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/transparency-linked-data'>transparency-linked-data </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bio2rdf-lsr'>bio2rdf-lsr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/ckan'>ckan </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/eu-agencies-bodies'>eu-agencies-bodies </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/aemet'>aemet </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/open-food-facts'>open-food-facts </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/linked-life-data'>linked-life-data </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-coreyavis-com'>statusnet-coreyavis-com </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/enipedia'>enipedia </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/german-labor-law-thesaurus'>german-labor-law-thesaurus </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/bpr'>bpr </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/gesis-thesoz'>gesis-thesoz </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/elviajero'>elviajero </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/verrijktkoninkrijk'>verrijktkoninkrijk </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-unlocode'>rkb-explorer-unlocode </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/dbpedia-de'>dbpedia-de </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-epsrc'>rkb-explorer-epsrc </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/iserve'>iserve </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/hellenic-police'>hellenic-police </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/statusnet-thelovebug-org'>statusnet-thelovebug-org </a></td><td></td><td></td></tr><tr><td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/chronicling-america'>chronicling-america </a></td><td></td><td></td></tr>
					</tbody>
				</table>
        	</div>
        </div>
        <div class="row" id="source">
            <div class="col-lg-8 col-lg-offset-2">
		        </br>
		        </br>
		        </br>
                <h4>Download:</h4>
                <table id='resources' style='font-size:11px'>
                <thead>
                	<tr>
                		<th align='center'>Description</th>
                		<th align='center'>URL</th>
                	</tr>
                </thead>
                <tbody>
                	<tr>
                		<td align='left'>Zip file containing a plain text file for each crawled dateset.</td>
                		<td align='center'><a href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>resources/plainTextRepository.zip">Download</a></td>
                	</tr>
                	<tr>
                		<td align='left'>Database file providing all entities,categories, fingerprint extracted for each dataset</td>
                		<td align='center'><a href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>resources/profileDB.sql">Download</a></td>
                	</tr>
                	<tr>
                		<td align='left'>War file of the application.</td>
                		<td align='center'><a href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>resources/">Download</a></td>
                	</tr>
                </tbody>
                </table>
        	</div>
        </div>
    </section>
   	
    <!-- About Section -->
    <section id="about" class="container content-section text-center">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2">
                <h2>Developed by:</h2>
                <p>Alexander Arturo Mera Caraballo, Bernardo Pereira Nunes, Marco Antonio Casanova.                    
                </p>
                <p>{acaraballo,bnunes,casanova}@inf.puc-rio.br.</p>
            </div>
        </div>
    </section>
    <!-- Contact Section -->
	</br>
	</br>
	</br>
	</br>
    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p style="float:left"><img src="http://www.puc-rio.br/imagens/brasao.jpg"  /></p>
        	<p>Pontifical Catholic University of Rio de Janeiro</p>
            <p>Copyright &copy; PUC-RIO 1992 - 2016.</p>
            <p style="float:right"><img src="http://www.inf.puc-rio.br/wp-content/themes/webdi2/imgs/logo_di1.png"  /></p>
        </div>
    </footer>

    <!-- Bootstrap Core JavaScript -->
    <script src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/bootstrap.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.easing.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/grayscale.js"></script>

</body>
</html>
