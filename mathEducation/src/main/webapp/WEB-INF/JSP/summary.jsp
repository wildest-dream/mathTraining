<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page isELIgnored="false" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>summary ${userid} </title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://unpkg.com/mathjs@6.0.3/dist/math.min.js"></script>
<script src="https://cdn.plot.ly/plotly-1.35.2.min.js"></script>
<script type="text/x-mathjax-config">
  MathJax.Hub.Config({tex2jax: {inlineMath: [['\\(','\\)']]}});
</script>
<script type="text/javascript" async
  src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML">
</script>
</head>
<body>
<script>
function draw(expression,range1,range2,divID) {//draw function plot according to expression
    try {
      // compile the expression once
      const expr = math.compile(expression);
      // evaluate the expression repeatedly for different values of x
      var xValues=math.range(range1,range2,(range2-range1)/100).toArray();
      var yValues=xValues.map(function (x){
    	  return expr.evaluate({x:x});
      });
      // render the plot using plotly
      const trace = {
        x: xValues,
        y: yValues,
        type: 'scatter',
        mode: 'lines',
        marker: {
        	color: 'black'
        },
        showlegend: false
      };
      const layout={
    		  xaxis: {
    			  title: 'x',
    			  showgrid: true
    		  },
    	      yaxis: {
    			  title: 'y',
    			  showgird: true
    		  }
      };
      const data = [trace];
      Plotly.plot(divID, data,layout);
    }
    catch (err) {
      console.error(err);
      alert(err);
    }
  }
  function drawline(x1,y1,x2,y2,ifImaginary,divID){//draw line segment between two points
	  try{
		  var xValues;
		  var yValues;
		  if(x1!=x2){
			  xValues=math.range(x1,x2,(x2-x1)/100).toArray();
			  yValues=xValues.map(function (x){
		    	  return ((y2-y1)*x+y1*x2-y2*x1)/(x2-x1);
		      });
		  }else{
			  yValues=math.range(y1,y2,(y2-y1)/100).toArray();
			  xValues=[x1];
			  for(var i=1;i<yValues.length;i++){
				  xValues.push(x1);
			  }
		  }
		  const trace = {
				  x: xValues,
			      y: yValues,
			      type: 'scatter',
			      mode: ifImaginary?'markers':'lines',
			      marker: {
			    	  color: 'black',
			    	  size:1
			      },
			      showlegend: false
			      };
		  const layout={
				  xaxis: {
					  title: 'x',
			          showgrid: true
			      },
			      yaxis: {
			    	  title: 'y',
			    	  showgird: true
			      }
			      };
			      const data = [trace];
			      Plotly.plot(divID, data,layout);  
	  }
	  catch (err) {
	      console.error(err);
	      alert(err);
	    }
  }
  function mark(xValues,yValues,textArray,divID){//mark a point with some text
	  try{
		  const trace = {
			        x: xValues,
			        y: yValues,
			        type: 'scatter',
			        mode: 'text',
			        text: textArray,
			        showlegend: false
			      };
			      const layout={
			    		  xaxis: {
			    			  title: 'x',
			    			  showgrid: true
			    		  },
			    	      yaxis: {
			    			  title: 'y',
			    			  showgird: true
			    		  }
			      };
			      const data = [trace];
			      Plotly.plot(divID, data,layout);
	  }
	  catch (err) {
		  console.error(err);
	      alert(err);
	  }
  }
</script>
<table align="left">
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Tomato;">User ID: ${user_id}</td>
        <td style="width:100%;font-style: italic;font-size:200%;"><a href="logout"><u>Log out</u></a></td>
    </tr>
    <tr><td><form action="home" method="post">
            ${questions}<br>
            <input type="hidden" name="page" value="summary">
            <input type="submit" value="Return Home Page" style="corlor:Blue">
    </form></td></tr>
</table>
</body>
</html>