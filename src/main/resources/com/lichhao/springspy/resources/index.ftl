<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="utf-8">
		<title>SpringSpy-index</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="bootstrap/css/bootstrap.css">
		<link rel="stylesheet" href="bootstrap/css/bootstrap-responsive.css">
		<link rel="icon shortcut" href="img/logo.jpg">
		<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
	      <script src="bootstrap/js/html5shiv.js"></script>
	    <![endif]-->
		<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
tr td{
	word-break:break-all;
}
</style>
	</head>

	<body>
		<div class="navbar navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<a class="brand" href="index.html">SpringSpy</a>
					<div class="nav-collapse collapse">
						<a id="refresh" class="btn btn-primary pull-right" href="refresh">Refresh Container</a>
					</div>
					<!--/.nav-collapse -->
				</div>
			</div>
		</div>

		<div class="container">
			<div class="row">
				<div class="span12">
					<div class="well">
						<h3>Overview of current Spring container</h3>
						<p>click on the beanName column to view the detail of that bean, including everything you can find in the BeanDefinition data structure</p>
					</div>
					<table id="table1" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th>
									beanName
								</th>
								<th>
									beanClass
								</th>
								<th>
									beanType
								</th>
								<th>
									isLazyInit
								</th>		
								<th>
									parentBean
								</th>	
							</tr>
						</thead>
						<tbody>
						<#list beanDefinitionMap?keys as beanName>
						<#assign beanDefinition=beanDefinitionMap[beanName]>
						<#if beanDefinition.singleton>
							<#assign beanType='singleton'>
						<#elseif beanDefinition.prototype>
							<#assign beanType='prototype'>
						<#else>
							<#assign beanType='unknown'>
						</#if>
						<tr>
							<td><a>${beanName}</a></td>
							<td>${beanDefinition.beanClassName!''}</td>
							<td>${beanType}</td>
							<td>${beanDefinition.lazyInit?string("true", "false")}</td>
							<td><#if beanDefinition.parentName??><a>${beanDefinition.parentName}</a></#if></td>
						</tr>
						</#list>						
						</tbody>
					</table>
					
					<div class="pagination pagination-right">
						<span><font>total: ${total}, displaying ${begin} to ${end}</font></span>
						<#if pagination??>
						<ul>
						<#list pagination.pageItems as pageitem>
							<li<#if pageitem.style??> class="${pageitem.style}"</#if>><a<#if pageitem.href??> href="${pageitem.href}"</#if>>${pageitem.content!''}</a></li>
						</#list>
							<#--
							<li class="disabled"><a href="#">«</a></li>
							<li class="active"><a href="index.html?page=1">1</a></li>
							<li><a href="index.html?page=2">2</a></li>
							<li><a href="index.html?page=3">3</a></li>
							<li><a href="index.html?page=4">4</a></li>
							<li><a href="index.html?page=5">5</a></li>
							<li><a href="#">»</a></li>
							-->
						</ul>
						</#if>
					</div>
					<footer>
						<p>
							<a href="index.html">back to index</a>
							<a class="pull-right" href="javascript:history.go(-1);">back to last page</a>
						</p>
					</footer>
				</div>
			</div>
		</div>

		<script src="js/jquery-1.8.3.js"></script>
		<script src="bootstrap/js/bootstrap.js"></script>
		<script type="text/javascript">
	$(function() {
		$('#refresh').click(function(e){
			if(confirm('You are going to refresh the Spring container! Do you want to continue?')){
				return true;
			}else{
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		});
		$('td a').each(function(){
			var a = $(this);
			a.attr('href','detail.html?beanName='+encodeURIComponent(a.text()));
		});
	});
</script>

	</body>
</html>
