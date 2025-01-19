    package com.example.demo.web;

    import com.example.demo.model.Stock;
    import com.example.demo.service.StockService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/currency")
    @CrossOrigin(origins="*")
    public class StockController {

        private final StockService stockService;

        @Autowired
        public StockController(StockService stockService) {
            this.stockService = stockService;
        }

        @GetMapping("/company/{companyName}/date/{date}")
        public ResponseEntity<List<Stock>> getStockByCompanyAndDate(
                @PathVariable String companyName,
                @PathVariable String date) {

            List<Stock> stockData = stockService.getStockByCompanyAndDate(companyName, date);

            if (!stockData.isEmpty()) {
                return new ResponseEntity<>(stockData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @PostMapping("/save")
        public ResponseEntity<Stock> saveStock(@RequestBody Stock stock) {
            Stock savedStock = stockService.saveStock(stock);
            return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
        }

        @DeleteMapping("/{companyName}/{date}")
        public ResponseEntity<Void> deleteStock(@PathVariable String companyName, @PathVariable String date) {
            stockService.deleteStock(companyName, date);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
