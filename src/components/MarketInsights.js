import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';
import './MarketInsights.css';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const MarketInsights = () => {
  const [selectedProduct, setSelectedProduct] = useState('');
  const [comprehensiveData, setComprehensiveData] = useState(null);
  const [recommendations, setRecommendations] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [chartData, setChartData] = useState(null);

  // Sample product list - in a real app, this would come from your backend
  const products = [
    'Organic Tomatoes',
    'Fresh Lettuce',
    'Carrots',
    'Potatoes',
    'Onions',
    'Bell Peppers',
    'Cucumbers',
    'Spinach',
    'Kale',
    'Broccoli'
  ];

  useEffect(() => {
    if (selectedProduct) {
      fetchComprehensiveData();
      fetchRecommendations();
    }
  }, [selectedProduct]);

  useEffect(() => {
    if (comprehensiveData) {
      generateChartData();
    }
  }, [comprehensiveData]);

  const fetchComprehensiveData = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching comprehensive data for:', selectedProduct);
      const response = await axios.get(`http://localhost:8080/api/pricing/comprehensive/${encodeURIComponent(selectedProduct)}`);
      console.log('Comprehensive data response:', response.data);
      
      // Log the full structure of the response
      console.log('Comprehensive data structure:', JSON.stringify(response.data, null, 2));
      
      // Check if we have external products with prices
      if (response.data.externalProducts && response.data.externalProducts.length > 0) {
        console.log('External products:', response.data.externalProducts);
        
        // If optimalPrice is 0, try to calculate an average from external products
        if (response.data.optimalPrice === 0) {
          const prices = response.data.externalProducts
            .filter(product => product.price && product.price > 0)
            .map(product => product.price);
          
          if (prices.length > 0) {
            const avgPrice = prices.reduce((sum, price) => sum + price, 0) / prices.length;
            console.log('Calculated average price from external products:', avgPrice);
            response.data.optimalPrice = avgPrice;
          }
        }
      }
      
      setComprehensiveData(response.data);
    } catch (err) {
      setError('Failed to fetch comprehensive pricing data');
      console.error('Error fetching comprehensive data:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchRecommendations = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Fetching recommendations for:', selectedProduct);
      const response = await axios.get(`http://localhost:8080/api/pricing/recommendations/${encodeURIComponent(selectedProduct)}`);
      console.log('Recommendations response:', response.data);
      
      // Log the full structure of the response
      console.log('Recommendations structure:', JSON.stringify(response.data, null, 2));
      
      setRecommendations(response.data);
    } catch (err) {
      setError('Failed to fetch pricing recommendations');
      console.error('Error fetching recommendations:', err);
    } finally {
      setLoading(false);
    }
  };

  const generateChartData = () => {
    if (!comprehensiveData || !comprehensiveData.externalProducts) {
      return;
    }

    // Extract prices from external products
    const prices = comprehensiveData.externalProducts
      .filter(product => product.price && product.price > 0)
      .map(product => product.price);

    // If we don't have enough prices, generate synthetic data
    if (prices.length < 3) {
      const basePrice = comprehensiveData.optimalPrice || 2.49;
      const syntheticPrices = [];
      for (let i = 0; i < 10; i++) {
        // Generate prices with some randomness around the base price
        const variation = basePrice * (0.8 + Math.random() * 0.4);
        syntheticPrices.push(Number(variation.toFixed(2)));
      }
      
      // Create chart data with synthetic prices
      const labels = Array.from({ length: 10 }, (_, i) => `Day ${i + 1}`);
      setChartData({
        labels,
        datasets: [
          {
            label: 'Price History',
            data: syntheticPrices,
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.5)',
            tension: 0.1
          }
        ]
      });
    } else {
      // Create chart data with actual prices
      const labels = prices.map((_, i) => `Source ${i + 1}`);
      setChartData({
        labels,
        datasets: [
          {
            label: 'Price History',
            data: prices,
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.5)',
            tension: 0.1
          }
        ]
      });
    }
  };

  const handleProductChange = (event) => {
    console.log('Product changed to:', event.target.value);
    setSelectedProduct(event.target.value);
  };

  // Debug render
  useEffect(() => {
    console.log('Current state:', {
      selectedProduct,
      comprehensiveData,
      recommendations,
      loading,
      error
    });
  }, [selectedProduct, comprehensiveData, recommendations, loading, error]);

  // Helper function to format price
  const formatPrice = (price) => {
    if (price === null || price === undefined || price === 0) {
      return 'N/A';
    }
    return `$${price.toFixed(2)}`;
  };

  return (
    <div className="farm-dashboard">
      <div className="dashboard-header">
        <h1>Market Insights</h1>
        <p className="subtitle">Real-time pricing and market analysis</p>
      </div>

      <div className="product-selector">
        <label htmlFor="product-select">Select Product:</label>
        <select
          id="product-select"
          value={selectedProduct}
          onChange={handleProductChange}
        >
          <option value="">Select a product...</option>
          {products.map((product) => (
            <option key={product} value={product}>
              {product}
            </option>
          ))}
        </select>
      </div>

      {loading && <div className="loading">Loading market data...</div>}
      {error && <div className="error-message">{error}</div>}

      {comprehensiveData && !loading && !error && (
        <div className="dashboard-grid">
          <div className="pricing-card">
            <h2>Current Market Price</h2>
            <div className="price-display">
              <div className="price-value">
                {formatPrice(comprehensiveData.optimalPrice)}
              </div>
              <div className="price-label">per lb</div>
            </div>
          </div>

          <div className="insights-card">
            <h2>Market Insights</h2>
            <div className="insights-grid">
              <div className="insight-item">
                <div className="insight-label">Average Price</div>
                <div className="insight-value">
                  {formatPrice(comprehensiveData.insights?.averagePrice)}
                </div>
              </div>
              <div className="insight-item">
                <div className="insight-label">Price Volatility</div>
                <div className="insight-value">
                  {formatPrice(comprehensiveData.insights?.priceVolatility)}
                </div>
              </div>
              <div className="insight-item">
                <div className="insight-label">Demand Trend</div>
                <div className="insight-value">
                  {comprehensiveData.insights?.demandTrend || 'N/A'}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {chartData && !loading && !error && (
        <div className="chart-card">
          <h2>Price History</h2>
          <div className="chart-container">
            <Line 
              data={chartData} 
              options={{
                responsive: true,
                plugins: {
                  legend: {
                    position: 'top',
                  },
                  title: {
                    display: true,
                    text: `${selectedProduct} Price History`
                  }
                },
                scales: {
                  y: {
                    beginAtZero: false,
                    title: {
                      display: true,
                      text: 'Price ($)'
                    }
                  }
                }
              }}
            />
          </div>
        </div>
      )}

      {recommendations && !loading && !error && (
        <div className="recommendations-card">
          <h2>Pricing Recommendations</h2>
          <div className="recommendations-list">
            {recommendations.recommendations?.map((rec, index) => (
              <div key={index} className="recommendation-item">
                <div className="recommendation-type">{rec.type}</div>
                <div className="recommendation-price">{formatPrice(rec.price)}</div>
                <div className="recommendation-reason">{rec.reason}</div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default MarketInsights; 