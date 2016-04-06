<?xml version="1.0" encoding="ISO-8859-1" ?>  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-br">
<head>
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
	<script type="text/javascript" charset="utf8" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery-1.10.2.min.js"></script>
  	<script type="text/javascript" charset="utf8" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.dataTables.js"></script>
  	
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.ui.effects.js"></script>  
    <script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery-ui.min.js"></script>    
    <script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>js/jquery.form.js"></script>    
    <link rel="stylesheet" href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/jquery-ui.css" media="all" />
	<link rel="stylesheet" href="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>css/estilos.css" media="all" />
	<script type="text/javascript" src="<%=config.getServletContext( ).getInitParameter( "DIR_INCLUDES" ) %>library/jquery.cookie.js" ></script>
	
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
	<br>
	<br>
	<br>
	<br>
	<br>
    <!-- Intro Header -->
    <header class="intro">
        <div class="intro-body">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                    
                    <b style="font-size:18px;">What is the DRX tool?</b>
					<p style="font-size:16px;" align="justify">DRX is a tool to assist data publishers in the process of browsing and finding related datasets in the LOD cloud. 
					DRX takes advantage of various methods including crawling, profiling, clustering and ranking modules to create ranked 
					lists of related datasets.</br>
					One of the main contributions of DRX is the creation of standardized descriptions of LD datasets.
					Each dataset is represented by a normalized vector of 23 dimensions (Agriculture, Applied science, Arts, Belief, Business, Chronology, Culture, Education, Environment, Geography, Health, History, Humanities, Language, Law, Life, Mathematics, Nature, People, Politics, Science, Society, Technology) extracted from Wikipedia.
					<br>A full description of DRX can be <a target="_blank" href="http://www.semantic-web-journal.net/system/files/swj1267.pdf">found here</a>.</p>

					<b style="font-size:18px;">To whom and to what DRX is intended for?</b>
					<p style="font-size:16px;" align="justify">Data publishers who wants to:<br>
					(1) make their own dataset available through a standardized representation (we call it as "dataset fingerprints").
					<br>(2) find related datasets based on fingerprint similarities.</p>

					<b style="font-size:18px;">Does DRX generate links to other datasets?</b>
					<p style="font-size:16px;" align="justify">No. DRX provides a ranked list of datasets that may be related to a given one. To generate links between your dataset and the ones suggested by DRX you can use link discovery tools such as LIMES (link) and Silk (link).</p>

					<b style="font-size:18px;">What is the difference between Mannheim Catalog and DRX?</b>
					<p style="font-size:16px;" align="justify">DRX provides a standardized representation (fingerprints) for LD datasets whereas the Mannheim Catalog providesmetadata that was derived from the "Linked Data crawl" and community-provided metadata.
					<br>The standard representation generated by DRX facilitates the process of comparing and finding related datasets.</p> 

					<b style="font-size:18px;">Is DRX open-source?</b>
					<p style="font-size:16px;" align="justify">Yes. DRX is an open source application and the files can be downloaded at GitHub: <a target='_blank' href="https://github.com/alexandermera/DRX">Click here!!!</a></p> 

					<b style="font-size:18px;">How can I create a profile for my dataset?</b>
					<p style="font-size:16px;" align="justify">First you need to register your dataset, resources and data in the <a target="_blank" href="http://linkeddatacatalog.dws.informatik.uni-mannheim.de/">Mannheim Catalog</a>. After that, just click in the item menu <a target="_blank" href="http://drx.inf.puc-rio.br:8181/Approach/register.jsp"> Add a new dataset</a> at DRX home page and add the link to your dataset in the Mannheim catalog. We do the rest for you :)</p>

					<b style="font-size:18px;">Why do we use Mannheim Catalog?</b>
					<p style="font-size:16px;" align="justify">Mannheim is a well-established LD catalog that does not provide browsing and recommendation facilities to data publishers. So, intead of creating a new dataset catalog, we use them as seed to our tool and provide new features.</p>

					<b style="font-size:18px;">How can I contribute to the development of DRX?</b>
					<p style="font-size:16px;" align="justify">You can send suggestions to: {acaraballo,bnunes,casanova}@inf.puc-rio.br or use the GitHub to add new features.</p>

					<b style="font-size:18px;">Who maintains DRX?</b>
					<p style="font-size:16px;" align="justify">DRX is developed by the Department of Informatics of PUC-Rio and is supported by FAPERJ and CNPq funding agencies.</p>

					<b style="font-size:18px;">How can I cite DRX?</b>
					<p style="font-size:16px;" align="justify">This is a good question!</p>
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
