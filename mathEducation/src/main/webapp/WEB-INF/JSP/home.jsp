<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ page isELIgnored="false" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>home ${userid} </title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
</head>
<body>
<table align="left">
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Tomato;">User ID: ${userid}</td>
        <td style="width:100%;font-style: italic;font-size:200%;"><a href="logout"><u>Log out</u></a></td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Tomato;">User Level: ${level}</td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%; color: Tomato;">Total training time : ${trainingTime}</td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:400%; color: Violet;">Levels in each area:</td>
    </tr>
</table>
<p><br></p>
<table align="center" style="width:100%">    
    <tr>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level0}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level1}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level2}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level3}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level4}</td>
    </tr>
    <tr>
        <td>error rate:${error_rate0}<br>average time consumption:${average_time_consumption0}</td>
        <td>error rate:${error_rate1}<br>average time consumption:${average_time_consumption1}</td>
        <td>error rate:${error_rate2}<br>average time consumption:${average_time_consumption2}</td>
        <td>error rate:${error_rate3}<br>average time consumption:${average_time_consumption3}</td>
        <td>error rate:${error_rate4}<br>average time consumption:${average_time_consumption4}</td>
    </tr>
    <tr>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Probability </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Statistics </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Geometry </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Algebra </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Function </td>
    </tr>
    <tr><td style="font-size:200%"><br><br></td></tr>
    <tr>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level5}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level6}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level7}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level8}</td>
        <td align="center" style="font-style: normal;font-size:300%; color: SlateBlue;">${level9}</td>
    </tr>
    <tr>
        <td>error rate:${error_rate5}<br>average time consumption:${average_time_consumption5}</td>
        <td>error rate:${error_rate6}<br>average time consumption:${average_time_consumption6}</td>
        <td>error rate:${error_rate7}<br>average time consumption:${average_time_consumption7}</td>
        <td>error rate:${error_rate8}<br>average time consumption:${average_time_consumption8}</td>
        <td>error rate:${error_rate9}<br>average time consumption:${average_time_consumption9}</td>
    </tr>
    <tr>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Linear Algebra </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Trigonometry </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Analytic Geometry </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Calculus </td>
        <td align="center" style="font-style: italic;font-size:200%; color: MediumSeaGreen;">Discrete Mathematics </td>
    </tr>
</table>
<p><br></p>
<table align="left">
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%;color:DarkOrange"><form action="training" method="post">
        <input type="hidden" name="answer" value="">
        <input type="hidden" name="time" value="">
        <input type="hidden" name="submit_index" value="-1">
        <input type="submit" value="Start training!" ></form>
        </td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%;color:DarkOrchid"><form action="recommendation" method="post">
        <input type="hidden" name="answer" value="">
        <input type="hidden" name="time" value="">
        <input type="hidden" name="submit_index" value="-1">
        <input type="submit" value="Recommended for you!" ></form>
        </td>
    </tr>
    <tr>
        <td style="width:100%;font-style: italic;font-size:200%;color:DeepPink"><form action="error_review" method="post">
        <input type="hidden" name="answer" value="">
        <input type="hidden" name="time" value="">
        <input type="hidden" name="submit_index" value="-1">
        <input type="submit" value="Review your mistakes!" ></form>
        </td>
    </tr>
</table>
</body>
</html>