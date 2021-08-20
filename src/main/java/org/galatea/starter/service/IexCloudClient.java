package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "IexCloud", url = "${spring.rest.iexCloudPath}")
public interface IexCloudClient {

  /**
   * Gets the historically adjusted market-wide data for the last month for traded symbol
   * See https://iexcloud.io/docs/api/#historical-prices
   *
   * @param symbol symbol to retrieve historical data
   * @return list of historical price data for the symbol passed
   */
  @GetMapping("/stock/{symbol}/chart?token=${spring.rest.iexCloudToken}")
  public List<IexHistoricalPrice> getHistoricalPrices(@PathVariable String symbol);

  /**
   * Gets the historically adjusted market-wide data by the range specified for traded symbol
   * See https://iexcloud.io/docs/api/#historical-prices
   *
   * @param symbol symbol to retrieve historical data
   * @param range specified date range to fetch historical data
   * @return list of historical price data for the symbol passed
   */
  @GetMapping("/stock/{symbol}/chart/{range}?token=${spring.rest.iexCloudToken}")
  public List<IexHistoricalPrice> getHistoricalPricesByRange(@PathVariable String symbol,
      @PathVariable String range);

  /**
   * Gets the minute-by-minute data for the date specified for traded symbol
   * See https://iexcloud.io/docs/api/#historical-prices
   *
   * @param symbol symbol to retrieve historical data
   * @param date specified date to fetch historical data
   * @return list of historical price data for the symbol passed
   */
  @GetMapping("/stock/{symbol}/chart/date/{date}?token=${spring.rest.iexCloudToken}")
  public List<IexHistoricalPrice> getHistoricalPricesByDate(@PathVariable String symbol,
      @PathVariable String date);
}
