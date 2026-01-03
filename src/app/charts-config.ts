// src/app/charts-config.ts
import {
    Chart,
    LineElement,
    BarElement,
    PointElement,
    LineController,
    BarController,
    CategoryScale,
    LinearScale,
    Title,
    Tooltip,
    Legend,
    Filler
} from 'chart.js';

// Register all the components we need
Chart.register(
    LineElement,
    BarElement,
    PointElement,
    LineController,
    BarController,
    CategoryScale,  // x-axis
    LinearScale,    // y-axis
    Title,
    Tooltip,
    Legend,
    Filler
);