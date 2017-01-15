<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<!DOCTYPE html>
<html style="background-color:#fff">  
    <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="ECharts">
    <title>ECharts · Example</title>

    <link rel="shortcut icon" href="asset/ico/favicon.png">
    <link href="asset/css/font-awesome.min.css" rel="stylesheet">
    <link href="asset/css/bootstrap.css" rel="stylesheet">
    <link href="asset/css/carousel.css" rel="stylesheet">
    <link href="asset/css/echartsHome.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="./js/echarts.js"></script>
    <script src="asset/js/codemirror.js"></script>
    <script src="asset/js/javascript.js"></script>
    <style>
    .container-fluid {
        padding:0px;
        margin:0px;
    }
    </style>
    <link href="asset/css/codemirror.css" rel="stylesheet">
    <link href="asset/css/monokai.css" rel="stylesheet">
</head>

<body style="padding-top:12px">
    <div id="head" style="display:none"></div>
    <div class="container">
    	<div class="row">
    		<div class="col-md-4">
	    		<form role="form">
				  <div class="form-group">
				    <input type="text" class="form-control" placeholder="搜索关键字">
				  </div>
				</form>
			</div>
			<div class="col-md-8">
				<button type="submit" class="btn btn-default">Submit</button>
			</div>
    	</div>
    </div>
    <div class="container-fluid">
        <div class="row-fluid example">
            <div id="graphic" class="col-md-12">
                <div id="main" class="main" style="height:530px;"></div>
            </div>
        </div>
    </div>
    <!-- <footer id="footer"></footer> -->
    <script src="asset/js/jquery.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
    <script type="text/javascript" src="asset/js/echartsHome.js"></script>
    <script src="asset/js/bootstrap.min.js"></script>
    <script src="./js/job.js"></script>
</body>
</html>