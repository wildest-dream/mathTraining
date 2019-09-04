<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page isELIgnored="false" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>recommendation ${user_id}</title>
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
  //calculator functions, modified from https://code.sololearn.com/WN0dgrcEv5FY/#js
  function shiftFn(ken) {
    var shift = document.getElementById("shiftBtn") ;
    var sin=document.getElementById("sin");
    var cos=document.getElementById("cos");
    var tan=document.getElementById("tan");
    var log=document.getElementById("log");
    var PI=document.getElementById("PI");
    if (ken == 1) {
        shift.setAttribute("onclick", "shiftFn(0)") ;
        shift.style.backgroundColor = "orange" ;
        sin.innerHTML = "sin<sup>-1</sup>" ;
        sin.setAttribute("onclick", "trigo1('sin')") ;
        cos.innerHTML = "cos<sup>-1</sup>" ;
        cos.setAttribute("onclick", "trigo1('cos')") ;
        tan.innerHTML = "tan<sup>-1</sup>" ;
        tan.setAttribute("onclick", "trigo1('tan')") ;
        log.innerHTML = "ln" ;
        log.setAttribute("onclick", "log(0)") ;
        PI.innerHTML = "\u0065" ;
        PI.setAttribute("onclick", "piOrE('e')") ;
    } else {
        shift.setAttribute("onclick", "shiftFn(1)") ;
        shift.style.backgroundColor = "yellow" ;
        sin.innerHTML = "sin" ;
        sin.setAttribute("onclick", "trigo('sin')") ;
        cos.innerHTML = "cos" ;
        cos.setAttribute("onclick", "trigo('cos')") ;
        tan.innerHTML = "tan" ;
        tan.setAttribute("onclick", "trigo('tan')") ;
        log.innerHTML = "log" ;
        log.setAttribute("onclick", "log(1)") ;
        PI.innerHTML = "\u03C0" ;
        PI.setAttribute("onclick", "piOrE('pi')") ;
    }
}
function input(sun) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value += sun ;
    y.value += sun ;
}
function sqrt() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value += "sqrt(" ;
    y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
    " * sqrt(" : "sqrt(" ;
}
function leftParen() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value += "(" ;
    y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
    " * (" : "(" ;
}
function piOrE(lunar) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    if (lunar == "pi") {
        x.value += "\u03C0" ;
        y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
        " * PI" : "PI" ;
    } else {
        x.value += "\u0065" ;
        y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
        " * E" : " E" ;
    }
}
function log(jafca) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    if (jafca == 1) {
        x.value += "log(" ;
        y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
        " * log10(" : "log10(" ;
    } else {
        x.value += "ln(" ;
        y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
        " * log(" : "log(" ;
    }
}
function trigo(hatsyrei) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value += hatsyrei + "(" ;
    y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
    " * " + hatsyrei + "(PI / 180 * " : hatsyrei + "(PI / 180 * " ;
}
function trigo1(valentin) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value += valentin + "\u207B\u00B9("
    y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
    " * 180 / PI * a" + valentin + "(" : "180 / PI * a" + valentin + "(" ;
}
function multOrDiv(edward) {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    if (edward == "mult") {
        x.value += "\u00D7" ;
        y.value += "*" ;
    } else {
        x.value += "\u00F7" ;
        y.value += "/"
    }
}
function del() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    var z = document.getElementById("myAns") ;
    if (x.value.slice(-3) == "Ans") {
        y.value = (/[\d)IE]/.test(x.value.slice(-4, -3))) ? 
        y.value.slice(0, -(z.value.length + 3)) : y.value.slice(0, -(z.value.length)) ;
        x.value = x.value.slice(0, -3) ;
    } else if (x.value == "Error!") {
        ac() ;
    } else {
        switch (y.value.slice(-2)) {
            case "* ": // sin cos tan
            y.value = (/[\d)IE]/.test(x.value.slice(-5, -4))) ? 
            y.value.slice(0, -18) : y.value.slice(0, -15) ;
            x.value = x.value.slice(0, -4) ;
            break ;
            case "n(":
            case "s(": // asin acos atan
            y.value = (/[\d)IE]/.test(x.value.slice(-7, -6))) ? 
            y.value.slice(0, -19) : y.value.slice(0, -16) ;
            x.value = x.value.slice(0, -6) ;
            break ;
            case "0(": // log
            y.value = (/[\d)IE]/.test(x.value.slice(-5, -4))) ? 
            y.value.slice(0, -9) : y.value.slice(0, -6) ;
            x.value = x.value.slice(0, -4) ;
            break ;
            case "g(": // ln
            y.value = (/[\d)IE]/.test(x.value.slice(-4, -3))) ? 
            y.value.slice(0, -7) : y.value.slice(0, -4) ;
            x.value = x.value.slice(0, -3) ;
            break ;
            case "t(": // sqrt
            y.value = (/[\d)IE]/.test(x.value.slice(-6, -5))) ? 
            y.value.slice(0, -8) : y.value.slice(0, -5) ;
            x.value = x.value.slice(0, -5) ;
            break ;
            case "PI": // pi
            y.value = (/[\d)IE]/.test(x.value.slice(-2, -1))) ? 
            y.value.slice(0, -5) : y.value.slice(0, -2) ;
            x.value = x.value.slice(0, -1) ;
            break ;
            case " E": // e
            y.value = (/[\d)IE]/.test(x.value.slice(-2, -1))) ? 
            y.value.slice(0, -4) : y.value.slice(0, -2) ;
            x.value = x.value.slice(0, -1) ;
            break ;
            default:
            y.value = y.value.slice(0, -1) ;
            x.value = x.value.slice(0, -1) ;
        } ;
    }
}
function ac() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    x.value = y.value = "" ;
}
function ans() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    var z = document.getElementById("myAns") ;
    x.value += "Ans" ;
    y.value += (/[\d)IE]/.test(y.value.slice(-1))) ? 
    " * " + z.value : z.value ;
}
function equal() {
    var x = document.getElementById("result") ;
    var y = document.getElementById("myPara") ;
    var z = document.getElementById("myAns") ;
    for (var i = 0; i < x.value.split("(").length - x.value.split(")").length; i++) {
        y.value += ")" ;
    }
    if (y.value != "") {
        x.value = y.value = z.value = math.evaluate(y.value) ;
    }
    if (!isFinite(x.value)) x.value = "Error!" ;
}
</script>
<table align="left" width="100%">
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Tomato;">User ID: ${user_id}</td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:300%; color: Violet;">Recommendation ${index+1}<br>Time used:   
        <label id="timer"></label>
        <p id="start" style="font-size:0px">${startTime}</p>
        <script>
        var d=new Date().getTime();
        var seconds=(d-d%1000)/1000-document.getElementById("start").innerHTML;
        var myVar = setInterval(myTimer, 1000);
        function myTimer() {
          seconds++;
          var min=(seconds-seconds%60)/60;
          var sec=seconds%60;
          if(min<10){
             if(sec<10){
                document.getElementById("timer").innerHTML = "0"+(seconds-seconds%60)/60+":0"+seconds%60;
             }else{
                document.getElementById("timer").innerHTML = "0"+(seconds-seconds%60)/60+":"+seconds%60;
             }
          }else{
             if(sec<10){
                document.getElementById("timer").innerHTML = (seconds-seconds%60)/60+":0"+seconds%60;
             }else{
                document.getElementById("timer").innerHTML = (seconds-seconds%60)/60+":"+seconds%60;
             }
          }
          if(seconds==600){alert("You haven't submitted your answer in 10 minutes, you'll have only 5 minutes to go!");}
          if(seconds>900){
        	  alert("You have used up your time, the page will jump to the next question now!");
        	  clearInterval(myVar);
    		  document.forms["answer_and_time"].submit();
          }
        }
        </script>
        </td>
    </tr>
    <tr>
        <td>${questionContent}</td>
        <td><input id=myPara type="hidden">
<input id=myAns type="hidden" value=0><p style="color:#CDBA1E">Calculator:</p><p style="color:#EFDB2F">Notice:use degree measure in trigonometric functions</p>
<table width="100%">
    <tr>
    <td colspan=5><input id=result readonly></td>
    </tr>        
    <tr>
    <td onclick="shiftFn(1)" id=shiftBtn style="text-align: center;border: 1px solid white;background-color:#CDBA1E">Shift</td>
    <td id="sin" onclick="trigo('sin')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">sin</td>
    <td id="cos" onclick="trigo('cos')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">cos</td>
    <td id="tan" onclick="trigo('tan')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">tan</td>
    <td onclick="input('!')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">!</td>
    </tr>
    <tr>
    <td onclick="input('^')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">^</td>
    <td onclick="sqrt()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">sqrt</td>
    <td onclick="leftParen()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">(</td>
    <td onclick="input(')')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">)</td>
    <td id="log" onclick="log(1)" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">log</td>
    </tr>
    <tr>
    <td onclick="input('7')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">7</td>
    <td onclick="input('8')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">8</td>
    <td onclick="input('9')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">9</td>
    <td onclick="del()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">DEL</td>
    <td onclick="ac()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">AC</td>
    </tr>
    <tr>
    <td onclick="input('4')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">4</td>
    <td onclick="input('5')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">5</td>
    <td onclick="input('6')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">6</td>
    <td onclick="multOrDiv('mult')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E;font-size: 16px;">&#x00D7;</td>
    <td onclick="multOrDiv('div')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E;font-size: 16px;">&#x00F7;</td>
    </tr>
    <tr>
    <td onclick="input('1')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">1</td>
    <td onclick="input('2')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">2</td>
    <td onclick="input('3')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">3</td>
    <td onclick="input('+')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E;font-size: 16px;">+</td>
    <td onclick="input('-')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E;font-size: 16px;">-</td>
    </tr>
    <tr>
    <td onclick="input('0')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">0</td>
    <td onclick="input('.')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">.</td>
    <td id="PI" onclick="piOrE('pi')" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">&#x03C0;</td>
    <td onclick="ans()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">Ans</td>
    <td onclick="equal()" style="text-align: center;border: 1px solid white;background-color:#CDBA1E">=</td>
    </tr>
</table></td></tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Blue;">Your answer:</td>
    </tr>
    <tr>
        <td><c:choose>
        <c:when test="${type==0}">
        <form id="answer_and_time" action="recommendation" method="post">
        <select name="answer">
          <option value="">Not Answered</option>
          <option value="T">True</option>
          <option value="F">False</option>
        </select><br>
        <input type="hidden" id="time0" name="time" value="-1">
        <input type="hidden" name="submit_index" value="${index}">
        <p><br>Based on what you are weak in and want to practice more,do you think this recommendation is what you need?Please answer this question first and select yes or no:</p>
        <select name="if_needed">
          <option value="">Not Selected</option>
          <option value="Y">Yes</option>
          <option value="N">No</option>
        </select>
        <input type="button" value="Next" onclick="submit_answer()">
        <script>function submit_answer(){
		  clearInterval(myVar);
		  document.getElementById("time0").value=seconds;
		  document.forms["answer_and_time"].submit();}</script>
        </form>
        </c:when>
        <c:when test="${type==1}">
        <form id="answer_and_time" action="recommendation" method="post">
        <select name="answer">
          <option value="">Not Answered</option>
          <option value="A">A</option>
          <option value="B">B</option>
          <option value="C">C</option>
          <option value="D">D</option>
        </select><br>
        <input type="hidden" id="time1" name="time" value="-1">
        <input type="hidden" name="submit_index" value="${index}">
        <p><br>Based on what you are weak in and want to practice more,do you think this recommendation is what you need?Please answer this question first and select yes or no:</p>
        <select name="if_needed">
          <option value="">Not Selected</option>
          <option value="Y">Yes</option>
          <option value="N">No</option>
        </select>
        <input type="button" value="Next" onclick="submit_answer()">
        <script>function submit_answer(){
		  clearInterval(myVar);
		  document.getElementById("time1").value=seconds;
		  document.forms["answer_and_time"].submit();}</script>
        </form>
        </c:when>
        <c:otherwise>
        <form id="answer_and_time" action="recommendation" method="post">
        <input type="text" name="answer" onkeydown="set_time()"><br>
        <input type="hidden" id="time2" name="time" value="-1">
        <input type="hidden" name="submit_index" value="${index}">
        <p><br>Based on what you are weak in and want to practice more,do you think this recommendation is what you need?Please answer this question first and select yes or no:</p>
        <select name="if_needed">
          <option value="">Not Selected</option>
          <option value="Y">Yes</option>
          <option value="N">No</option>
        </select>
        <input type="button" value="Next" onclick="submit_answer()">
        <script>function submit_answer(){
		  clearInterval(myVar);
		  document.getElementById("time2").value=seconds;
		  document.forms["answer_and_time"].submit();}
        function set_time(){
        	if(event.keyCode==13){
        		clearInterval(myVar);
      		    document.getElementById("time2").value=seconds;
        	}
        }</script>
        </form>
        </c:otherwise>
        </c:choose></td>
    </tr>
    <tr><td>
    <p style="color:#CDBA1E">Formulas you may need:</p>
    <c:choose>
    <c:when test="${category==0}">
    <p>Total Probability: \(P(A)=\Sigma_{i=1}^n P(A|E_i)P(E_i)\)<br>
    	Baye's Theorem: \(P(A|B)=\frac{P(B|A)P(A)}{P(B)}\)<br>
    	\(X\thicksim Bin(n,p):\ P(X=r)=C_r^m p^r (1-p)^{n-r},\ E(X)=np,\ Var(X)=np(1-p)\)<br>
    	\(X\thicksim Poi(\lambda):\ P(X=x)=\frac{e^{-\lambda}\lambda^x}{x!},\ E(X)=\lambda,\ Var(X)=\lambda\)<br>
    	\(X\thicksim Geo(p):\ P(X=x)=(1-p)^{x-1}p,\ E(X)=\frac{1}{p},\ Var(X)=\frac{1-p}{p^2}\)<br>
    	\(X\thicksim HG(r,N,n):\ P(X=x)=\frac{\binom r x \binom{N-r}{n-x}}{\binom N n}\ for\ x=max(0,n-(N-r)),\cdots,min(r,n)\)<br>\(\qquad\qquad\qquad\qquad E(X)=\frac{nr}{N},\ Var(X)=\frac{r(N-r)n(N-n)}{N^2(N-1)}\)<br>
    	\(X\thicksim U(a,b):f(x)=\frac{1}{b-a},\ E(X)=\frac{a+b}{2},\ Var(X)=\frac{(b-a)^2}{12}\)<br>
    	\(X\thicksim E(\lambda):f(x)=\lambda e^{-\lambda x}\ for\ x>0,\ E(X)=\frac{1}{\lambda},\ Var(X)=\frac{1}{\lambda^2}\)<br>
    	\(X\thicksim N(\mu,\sigma^2):f(x)=\frac{1}{\sigma \sqrt{2\pi}} e^{-\frac{(x-\mu)^2}{2\sigma^2}}\)<br>\(E(X)=\mu,\ Var(X)=\sigma^2\)</p>
    </c:when>
    <c:when test="${category==1}"><p>
    	Linear regression:\(\hat{y_i}=\hat{\beta_0}+\hat{\beta_1} X_i\)<br>\(\qquad\qquad\qquad\quad\hat{\beta_1}=\frac{\Sigma_{i=1}^n x_i Y_i - n\bar x \bar Y}{\Sigma_{i=1}^n x_i^2-n{\bar x}^2}\ \hat{\beta_0}=\bar Y-\hat{\beta_1}\bar x\)<br>
    	\(X\thicksim N(\mu,\sigma^2):f(x)=\frac{1}{\sigma \sqrt{2\pi}} e^{-\frac{(x-\mu)^2}{2\sigma^2}}\ E(X)=\mu,\ Var(X)=\sigma^2\)<br>
    	<img src='https://i.ibb.co/ZBZ3TCv/ztable.gif' alt='ztable' border='0'><br><img src='https://i.ibb.co/RHM6qr1/ttable.gif' alt='ttable' border='0'>
    </p></c:when>
    <c:when test="${category==2}"><p>Sphere volumn:\(V=\frac{4}{3}\pi r^3\qquad\)Sphere surface area:\(S=4\pi r^2\)
    </p></c:when>
    	<c:when test="${category==3}"><p>
    	Roots of quadratic equation \(ax^2+bx+c=0,a\ne 0\):\(x=\frac{-b\pm \sqrt{b^2-4ac}}{2a}\)<br>
    	\((a+b)^n=\Sigma_{k=0}^n \binom{n}{k}a^{n-k}b^k\)<br>
    	\(a^3+b^3=(a+b)(a^2-ab+b^2)\qquad a^n-b^n=(a-b)\Sigma_{k=0}^{n-1} a^{n-1-k}b^k\)
    </p></c:when>
    <c:when test="${category==4}"><p>
    	Vertex form of quadratic function \(f(x)=a^2 +bx+c,a\ne 0\):<br>\(f(x)=a(x-h)^2 +k,h=-\frac{b}{2a},k=f(-\frac{b}{2a})\)
    </p></c:when>
    <c:when test="${category==5}"><p>
    	Definition of Determinant:<br> $$A=\left\lgroup \matrix{a_{11}& a_{12}& .\ .\ .& a_{1n}\cr a_{21}& a_22& .\ .\ .& a_{2n}\cr .& .& .\ \ \ \ & .\cr .& .&\ \ .\ \ & .\cr .& .&\ \ \ \ .& .\cr a_{n1}& a_{n2}& .\ .\ .& a_{nn}}\right\rgroup$$
    	\(det(A)=\Sigma_{i_1,i_2,\cdots,i_n}^n\varepsilon_{i_1\cdots i_n}a_{1i_1}a_{2i_2}\cdots a_{ni_n}\)
    	, where \(\varepsilon_{i_1\cdots i_n}\) is the Levi-Civita symbol
    </p></c:when>
    <c:when test="${category==6}"><p>
    	\(\cos^2 A+\sin^2 A=1\)<br>\(\frac{a}{\sin A}=\frac{b}{\sin B}=\frac{c}{\sin C}\)<br>
    	\(a^2=b^2+c^2-2bc\cos A\)<br>\(\cos{(A-B)}=\cos A\cos B+\sin A\sin B\)<br>
    	\(\cos{(A+B)}=\cos A\cos B-\sin A\sin B\)<br>\(\cos 2A=\cos^2A-\sin^2A\)<br>
    	\(\sin 2A=2\sin A\cos A\)<br>\(\sin{(A+B)}=\sin A\cos B+\cos A\sin B\)<br>
    	\(\sin{(A-B)}=\sin A\cos B-\cos A\sin B\)<br>\(\tan{(A+B)}=\frac{\tan A+\tan B}{1-\tan A\tan B}\)
    	<br>\(\tan{(A-B)}=\frac{\tan A-\tan B}{1+\tan A\tan B}\)
    </p></c:when>
    <c:when test="${category==7}"><p>
    	The distance from \(P_1(x_1,y_1)\) to line \(Ax+By+C=0\) is \(d=\frac{|Ax_1+By_1+C|}{\sqrt{A^2+B^2}}\)
    	<br>Tangent line in a point \(D(x_0,y_0)\) of a Parabola \(y^2=2px\):\(\quad y_0y=p(x+x_0)\)
    	<br>Tangent line in a point \(D(x_0,y_0)\) of a Ellipse \(\frac{x^2}{a}+\frac{y^2}{b}=1\):\(\quad \frac{x_0x}{a}+\frac{y_0y}{b}=1\)
    	<br>Area of an Ellipse \(\frac{x^2}{a}+\frac{y^2}{b}=1\):\(\quad A=\pi ab\)
    	<br>Tangent line in a point \(D(x_0,y_0)\) of a Hyperbola \(\frac{x^2}{a}-\frac{y^2}{b}=1\):\(\quad \frac{x_0x}{a}-\frac{y_0y}{b}=1\)
    	<br>Angle between two planes:\(A_1x+B_1y+C_1z+D_1=0,A_2x+B_2y+C_2z+D_2=0\):
    	<br>\(\qquad\qquad\qquad\qquad\qquad \arccos{\frac{|A_1A_2+B_1B_2+C_1C_2|}{\sqrt{A_1^2+B_1^2+C_1^2}\sqrt{A_2^2+B_2^2+C_2^2}}} \)
    	<br>Distance from the point \(P(x_0,y_0,z_0)\) to the plane: \(Ax+By+C=0\): \(d=\frac{|Ax_0+By_0+Cz_0|}{\sqrt{A^2+B^2+C^2}}\)
    </p></c:when>
    <c:when test="${category==8}"><p>
    	\(\frac{d}{dx}x^n=nx^{n-1}\qquad\frac{d}{dx}(f(x)g(x))=f'(x)g(x)+f(x)g'(x)\)<br>
    	\(\frac{d}{dx}(\frac{f(x)}{g(x)})=\frac{f'(x)g(x)-f(x)g'(x)}{g^2(x)}\quad\frac{d}{dx}\sin x=\cos x\)<br>
    	\(\frac{d}{dx}\cos x=-\sin x\qquad\frac{d}{dx}\tan x=\sec^2x\)<br>
    	\(\frac{d}{dx}\cot x=-\csc^2x\qquad\frac{d}{dx}\sec x=\sec x\tan x\)<br>
    	\(\frac{d}{dx}\csc x=-\csc x\cot x\qquad\frac{d}{dx}(e^x)=e^x\qquad\frac{d}{dx}(a^x)=e^x\ln a\)<br>
    	\(\frac{d}{dx}\ln x=\frac{1}{x}\quad\frac{d}{dx}(\arcsin x)=\frac{1}{\sqrt{1-x^2}}\quad\frac{d}{dx}(\arctan x)=\frac{1}{1+x^2}\)<br>
    	\(\int\frac{dx}{\sqrt{a^2-x^2}}=\arcsin \frac{x}{a}+C\quad\int\frac{dx}{a^2+x^2}=\frac{1}{a}\arctan{\frac{x}{a}}+C\)<br>
    	\(\int\frac{dx}{\sqrt{x^2+a^2}}=\ln(x+\sqrt{x^2+a^2})+C\quad\int\frac{dx}{a^2-x^2}=\frac{1}{2a}\ln(\frac{a+x}{a-x}+C)\)<br>
    	\(\int\sin^2 xdx=\frac{x}{2}-\frac{1}{4}\sin 2x+C\quad\int\sin^3 xdx=\frac{1}{3}\cos^3 x-\cos x+C\)
    </p></c:when>
    <c:otherwise><p>
        \(n^{th}\) term of an arithmetic progression:\(t_n=t_1+(n-1)d\)<br>
        Sum of first n terms in an arithmetic progression:\(S_n=\frac{n}{2}[2t_1+(n-1)d]\)
        <br>\(n^{th}\) term of a geometric progression(GP):\(t_n=t_1r^{n-1}\)
        <br>Sum of first n terms in a geometric progression(GP):$$S_n=\cases{\frac{t_1(r^n-1)}{r-1}\quad\text{if }r>1\cr \frac{t_1(1-r^n)}{1-r}\quad\text{if }r<1}$$
        \(Permutation:P(n,r)=\frac{n!}{(n-r)!}\quad Combination:C(n.r)=\frac{n!}{(n-r)!r!}\)<br>
        Euler's formula:\(v+f=e+2\),\(v\) is the verticle, \(e\) is the edge and \(f\) is the face (in a planar graph)
    </p></c:otherwise></c:choose>
    </td>
    </tr>
</table>
</body>
</html>