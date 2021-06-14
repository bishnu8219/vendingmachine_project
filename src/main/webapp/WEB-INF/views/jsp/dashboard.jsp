<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!-- full jquery -->
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>

<!-- bootstrap css -->
<link rel="stylesheet"
	href="${path}/webjars/bootstrap/3.3.5/css/bootstrap.min.css">

<!-- start custom css and js-->
<link rel="stylesheet" type="text/css" href="${path}/css/main.css" />
<!-- end custom css and js -->

<div class="panel panel-default">

	<form:form method="post" class="form-horizontal" id="vendingform"
		modelAttribute="vendingrequest" action="${path}/vending/vendingresult">
		<div class="panel-body">

			<div class="form-group" id="form-group1">
				<label class="col-sm-3">Coins Inserted</label> <label
					class="col-sm-3">Product Available</label>

			</div>

			<div class="form-group" id="form-group2">

				<div class="col-sm-1">
					<form:label path="usercoin" id="usercoin" name="usercoin"
						class="usercoin">
					</form:label>
				</div>

				<div class="col-sm-1">
					<a href="javascript:void(0);" id="insertcoin"><span
						class="glyphicon"></span>Insert Coin</a>
				</div>

				<div class="col-sm-1">
					<a href="javascript:void(0);" id="ejectcoin"><span
						class="glyphicon"></span>Eject Coin</a>
				</div>


				<div class="col-sm-2">
					<form:select path="producttype" id="producttype" name="producttype"
						class="producttype">
						<form:options items="${productTypeList}" />
					</form:select>
				</div>

				<div class="col-sm-1">
					<a href="javascript:void(0);" id="dispenseproduct"><span
						class="glyphicon"></span>Dispense Soda</a>

				</div>

				<div class="col-sm-1">
					<a href="javascript:void(0);" id="sodacount"><span
						class="glyphicon"></span>Soda Count</a>

				</div>

				<div class="col-sm-1">
					<a href="javascript:void(0);" id="salesreport"><span
						class="glyphicon"></span>Sales Report</a>

				</div>

			</div>
		</div>

		<div id="resultContainer" style="display: none;">
			<hr />
			<h4 style="color: green;">Result</h4>
			<pre style="color: green;">
			    <code></code>
			   </pre>
		</div>

	</form:form>

</div>


<script>
	$(document).ready(function() {
		$('#resultContainer').hide();
	});

	function insertCoin() {
		data = $('#vendingform').serialize(), url = "/vending/insertcoin";
		// Send the data using post
		var posting = $.post(url, data);
		// Put the results in a div
		posting
				.done(function(res) {
					if (res.validated) {
						//Set response
						$('#resultContainer pre code')
								.text(
										JSON
												.stringify(
														JSON
																.parse(res.vendingrequest.vendingresult).result,
														null, '\t'));
						//$('#resultContainer').show();
						var userCoins = JSON
								.stringify(JSON
										.parse(res.vendingrequest.vendingresult).result["coinscount"]);
						$("#usercoin").text(userCoins);
					} else {
						//Set error messages
						$.each(res.errorMessages,
								function(key, value) {
									$('input[name=' + key + ']').after(
											'<span class="error">' + value
													+ '</span>');
								});
					}
				});

	}

	function ejectCoin() {
		data = $('#vendingform').serialize(), url = "/vending/ejectcoin";
		// Send the data using post
		var posting = $.post(url, data);
		// Put the results in a div
		posting
				.done(function(res) {
					if (res.validated) {
						//Set response
						$('#resultContainer pre code')
								.text(
										JSON
												.stringify(
														JSON
																.parse(res.vendingrequest.vendingresult).result,
														null, '\t'));
						//$('#resultContainer').show();
						var userCoins = JSON
								.stringify(JSON
										.parse(res.vendingrequest.vendingresult).result["coinscount"]);
						$("#usercoin").text(userCoins);
					} else {
						//Set error messages
						$.each(res.errorMessages,
								function(key, value) {
									$('input[name=' + key + ']').after(
											'<span class="error">' + value
													+ '</span>');
								});
					}
				});

	}

	function sodaCount() {
		data = $('#vendingform').serialize(), url = "/vending/sodacount";
		// Send the data using post
		var posting = $.post(url, data);
		// Put the results in a div
		posting
				.done(function(res) {
					if (res.validated) {
						//Set response
						$('#resultContainer pre code')
								.text(
										JSON
												.stringify(
														JSON
																.parse(res.vendingrequest.vendingresult).result,
														null, '\t'));
						$('#resultContainer').show();
					} else {
						//Set error messages
						$.each(res.errorMessages,
								function(key, value) {
									$('input[name=' + key + ']').after(
											'<span class="error">' + value
													+ '</span>');
								});
					}
				});

	}

	function salesReport() {
		data = $('#vendingform').serialize(), url = "/vending/salesreport";
		// Send the data using post
		var posting = $.post(url, data);
		// Put the results in a div
		posting
				.done(function(res) {
					if (res.validated) {
						//Set response
						$('#resultContainer pre code')
								.text(
										JSON
												.stringify(
														JSON
																.parse(res.vendingrequest.vendingresult).result,
														null, '\t'));
						$('#resultContainer').show();
					} else {
						//Set error messages
						$.each(res.errorMessages,
								function(key, value) {
									$('input[name=' + key + ']').after(
											'<span class="error">' + value
													+ '</span>');
								});
					}
				});

	}

	function dispenseProduct() {
		data = $('#vendingform').serialize(), url = "/vending/dispenseproduct";
		// Send the data using post
		var posting = $.post(url, data);
		// Put the results in a div
		posting
				.done(function(res) {
					if (res.validated) {
						//Set response
						products = JSON.parse(res.vendingrequest.vendingresult).result["products"];
						var prodKey = [];
						var prodVal = [];
						$.each(products, function(i, item) {
							prodKey.push(i);
							prodVal.push(item);
						});

						var listItems = "";
						try {
							for (var i = 0; i < prodKey.length; i++) {
								listItems += "<option value='" + prodVal[i] + "'>"
										+ prodKey[i] + "</option>";
							}
							$("select#producttype").html(listItems);
							//update usercoins
							var userCoins = JSON
									.stringify(JSON
											.parse(res.vendingrequest.vendingresult).result["change"]["quarters"]);
							$("#usercoin").text(userCoins);

							$('#resultContainer pre code')
									.text(
											JSON
													.stringify(
															JSON
																	.parse(res.vendingrequest.vendingresult).result["change"],
															null, '\t'));
							$('#resultContainer').show();
						} catch (err) {

						}

					} else {
						//Set error messages
						$.each(res.errorMessages,
								function(key, value) {
									$('input[name=' + key + ']').after(
											'<span class="error">' + value
													+ '</span>');
								});
					}
				});

	}

	//button clicked event listener
	$("#insertcoin").click(function(e) {
		insertCoin();

	});

	//button clicked event listener
	$("#ejectcoin").click(function(e) {
		var userCoin = $("#usercoin").text();
		if (userCoin < 1) {
			alert("No coins to eject!");
		} else {
			ejectCoin();
		}

	});

	//button clicked event listener
	$("#sodacount").click(function(e) {
		sodaCount();

	});

	//button clicked event listener
	$("#salesreport").click(function(e) {
		salesReport();

	});

	//button clicked event listener
	$("#dispenseproduct").click(function(e) {
		//make sure machine has soda
		var availProduct = $("#producttype option:selected").text();
		if (availProduct == "") {
			alert("Out of Soda!");
		} else {
			var selProduct = $("#producttype option:selected").text();
			var msg = "Dispensing " + selProduct + " product?";
			var userCoins = $("#usercoin").text();
			//make sure user inserted coins
			if (userCoins < 1) {
				alert("Insert Coins first!");
			} else {
				if (confirm(msg)) {
					// Dispense it!
					dispenseProduct();
				} else {
					// Do nothing!

				}
			}
		}

	});
</script>
