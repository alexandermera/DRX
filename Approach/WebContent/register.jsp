<?xml version="1.0" encoding="ISO-8859-1" ?>  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br">
<head>
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
	<script type="text/javascript" charset="utf8" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery-1.10.2.min.js"></script>
  	<script type="text/javascript" charset="utf8" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.dataTables.js"></script>
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
                <a class="navbar-brand page-scroll" href="fingerprint.jsp">
                    <i class="fa fa-play-circle"></i>  <span class="light">Main Page</span>
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
                        <a class="page-scroll" href="#about">About</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Intro Header -->
    <header class="intro">
        <div class="intro-body">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                    <p>How can I create a profile for my dataset?</p>
                    <p>DRX is not a Catalog of datasets. It generates dataset profiles from datasets available in stablished catalogs such as Mannheim Linked Data catalog.</p>
                     
                    Before adding a new dataset in DRX, you must create an account and submit your dataset to the Mannheim Catalog. The idea of DRX is not create multiple copies of LD datasets, but 
                    
                    Follow the steps below to submit a new dataset to DRX:</p>
                    <ul>
                    	<li>Go to <a href="http://linkeddatacatalog.dws.informatik.uni-mannheim.de/" target="_blank">Mannheim Catalog</a>, create an account and register a new dataset.</li>
                    	<li>Add 'Data and Resources' to your entry in the Mannheim Catalog. For example: SPARQL endpoint, HTML, CSV, RDF, etc.</li>
                    	<li></li>
                    	<li></li>
                    </ul>
                    
						<fieldset>
  							<legend>Add new dataset</legend>
	  						Mannheim URL: <input style="text-align: center" type="text" size="40" id="dsName"/>
	  						<input type="button" value="Retrieve" id="retrieve"></input>
	  						<div id="status_indicator">&nbsp;</div>
							</div>
							<div>
	  						<span>Example:  <a target="_blank" href="http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-webscience">http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/rkb-explorer-webscience</a></span>
							</div> 
						</fieldset>
                    </div>
                </div>
            </div>
        </div>
    </header>
    
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
   
    <!-- Map Section -->
    <div></div>
	<br>
	<br>
	<br>
	<br>
    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p style="float:left"><img src="http://www.puc-rio.br/imagens/brasao.jpg"  /></p>
        	<p>Pontifical Catholic University of Rio de Janeiro</p>
            <p>Copyright &copy; PUC-RIO 1992 - 2015.</p>
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
