# CurrencyService

Program that provides functionality of Monobank, Privatbank and SaveInfo services.

Program allows to call Privatbank/Monobank services by using mapping "/privatbank" or "/monobank" before  calling method of controller
For e.g.:  *http://localhost:9800/**privatbank/..***
*http://localhost:9800/**monobank/...***

## Controller.java  

Controller calls methods depending on what service was called (mono/privat ) :  

 1. Getting currency
	 - 
Method getCurrencyFrom() returns sell/buy rate converted to UAH via provided date.
	 - data: String. Should be provided in format "yyyy.mm.dd"
	 - currencyFrom: String. Can be "USD/EUR/RUB" only
	 - currencyTo: String. "UAH" only
*E.g. /privatbank/**getcurrency?date=2021.03.27&currencyFrom=USD&currencyTo=UAH***
```java
@RequestMapping("/getcurrency")  
@GetMapping  
public ResponseEntity<ExchangeRate> getCurrencyFrom(@RequestParam String date,  
  @RequestParam String currencyFrom,  
  @RequestParam String currencyTo) {
  ...
  }
```
 2. Getting best currency for month
	 - 
Method bestCurrencyMonth() returns the best sell/buy rate converted to UAH withing month.
	 - currencyFrom: String. Can be "USD/EUR/RUB"
	 - currencyTo: String. "UAH" only
*E.g. /privatbank/**getbestcurrency/month?currencyFrom=EUR&currencyTo=UAH***
```java
@RequestMapping(value = "/getbestcurrency/month")  
@GetMapping  
public ExchangeRate bestCurrencyMonth(@RequestParam String currencyFrom,  
  @RequestParam String currencyTo) {
  ...
  }
```
 3. Getting .docx
	 - 
Method getDoc() returns .docx file with result of **getting currency** method to user
	 - currencyFrom: String. Can be "USD/EUR/RUB"
	 - currencyTo: String. "UAH" only
	 - date: String. Should be provided in format "yyyy.mm.dd"
E.g. */monobank/**saveExchangeRateToDo**c?currencyFrom=USD&currencyTo=UAH&date=2021.03.27*
```java
@RequestMapping(value = "/saveExchangeRateToDoc")  
@GetMapping  
public ResponseEntity<InputStreamResource> getDoc(@RequestParam String currencyFrom,  
  @RequestParam String currencyTo,  
  @RequestParam String date) {
...
}
 ```
## GetAllExchangeRates.java

 1. Getting all exchange rates
	 - 
Method getAllExchangeRates() allowas to get all exchange rates of mono and private banks by provided data. Method is multi-threaded.
```java
@RequestMapping(value="/getAllExchangeRates")  
@GetMapping  
public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(@RequestParam String currencyFrom,  
  @RequestParam String currencyTo,  
  @RequestParam String date) {
  ...
  }
 ```
 ## Getting xml/ json response
To get json response insert **.json** in your request.
To get xml response insert **.xml** in your request.
**Default response is represented in xml format.**

For e.g.: /getAllExchangeRates.**json**?currencyFrom=USD&currencyTo=UAH&date=2021.03.13
