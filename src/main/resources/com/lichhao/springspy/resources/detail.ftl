<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="utf-8">
		<title>SpringSpy-detail</title>
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
					<#-- 
						<ul class="nav pull-right">
							<li class="active">
								<a href="#home">首页</a>
							</li>
							<li>
								<a href="#about">关于我</a>
							</li>
							<li>
								<a href="#contact">留言</a>
							</li>
						</ul>
					-->
					</div>
					
					<!--/.nav-collapse -->
				</div>
			</div>
		</div>

		<div class="container">
			<div class="row">
				<div class="span12">
					<div class="well">
						<h3>Detail of bean: ${beanName}</h3>
						<p>show you everything we can know about ${beanName} from the BeanDefinition data structure of it</p>
					</div>
					<table class="table table-striped table-bordered table-condensed">
						<tbody>
						<tr>
							<td><strong>beanName</strong></td>
							<td>${beanName}</td>
						</tr>
						<tr>
							<td><strong>beanClassName</strong></td>
							<td>${beanDefinition.beanClassName!''}</td>
						</tr>						
						<tr>
							<td><strong>parentName</strong></td>
							<td><#if beanDefinition.parentName??><a>${beanDefinition.parentName}</a></#if></td>
						</tr>
						<tr>
							<td><strong>factoryBeanName</strong></td>
							<td>${beanDefinition.factoryBeanName!''}</td>
						</tr>
						<tr>
							<td><strong>factoryMethodName</strong></td>
							<td>${beanDefinition.factoryMethodName!''}</td>
						</tr>
						<tr>
							<td><strong>scope</strong></td>
							<td>${beanDefinition.scope!''}</td>
						</tr>						
						<tr>
							<td><strong>isLazyInit</strong></td>
							<td>${beanDefinition.lazyInit?string("true", "false")}</td>
						</tr>		
						<tr>
							<td><strong>dependsOn</strong></td>
							<td><#if beanDefinition.dependsOn??><#list beanDefinition.dependsOn as depend><a>${depend}</a><#if depend_has_next>, </#if></#list></#if></td>
						</tr>						
						<tr>
							<td><strong>isAutowireCandidate</strong></td>
							<td>${beanDefinition.autowireCandidate?string("true", "false")}</td>
						</tr>
						<tr>
							<td><strong>isPrimary</strong></td>
							<td>${beanDefinition.primary?string("true", "false")}</td>
						</tr>						
						<tr>
							<td><strong>constructorArgumentValues</strong></td>
							<td>${beanDefinition.constructorArgumentValues}</td>
						</tr>
						<tr>
							<td><strong>propertyValues</strong></td>
							<td>${beanDefinition.propertyValues}</td>
						</tr>						
						<tr>
							<td><strong>isSingleton</strong></td>
							<td>${beanDefinition.singleton?string("true", "false")}</td>
						</tr>				
						<tr>
							<td><strong>isPrototype</strong></td>
							<td>${beanDefinition.prototype?string("true", "false")}</td>
						</tr>							
						<tr>
							<td><strong>isAbstract</strong></td>
							<td>${beanDefinition.abstract?string("true", "false")}</td>
						</tr>
						<tr>
							<td><strong>role</strong></td>
							<td><#if beanDefinition.role==0>ROLE_APPLICATION<#elseif beanDefinition.role==1>ROLE_SUPPORT<#elseif beanDefinition.role==2>ROLE_INFRASTRUCTURE</#if></td>
						</tr>
						<tr>
							<td><strong>description</strong></td>
							<td>${beanDefinition.description!''}</td>
						</tr>						
						<tr>
							<td><strong>resourceDescription</strong></td>
							<td>${beanDefinition.resourceDescription!''}</td>
						</tr>						
						<tr>
							<td><strong>originatingBeanDefinition</strong></td>
							<td>${beanDefinition.originatingBeanDefinition!''}</td>
						</tr>																								
						</tbody>
					</table>
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
