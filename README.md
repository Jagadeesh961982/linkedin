# User Search Dashboard

A responsive and efficient user search application built with React and Material-UI. This project provides a clean interface to search, sort, and filter user data fetched from a public API. All data operations are performed on the client-side for a fast and seamless user experience.

![User Search Dashboard Screenshot](https://via.placeholder.com/800x450.png?text=User+Search+Dashboard+UI)
*(Note: You can replace the placeholder URL with an actual screenshot of your application)*

## Table of Contents

-   [Features](#features)
-   [Tech Stack](#tech-stack)
-   [Prerequisites](#prerequisites)
-   [Getting Started](#getting-started)
-   [Available Scripts](#available-scripts)
-   [Code Overview](#code-overview)
-   [How It Works](#how-it-works)

## Features

-   **Live Data Fetching**: Fetches a complete list of users from the `dummyjson.com` public API on application load.
-   **Client-Side Search**: Instantly searches for users by first name, last name, or SSN. A minimum of 3 characters is required to initiate a search.
-   **Client-Side Sorting**: Sorts the displayed users by age in either ascending or descending order.
-   **Role-Based Filtering**: Filters the user list based on their assigned role (`admin`, `moderator`, `user`).
-   **Responsive Design**: A clean, card-based layout built with Material-UI that adapts gracefully to various screen sizes.
-   **Loading & Error States**: Provides clear visual feedback to the user during the initial data fetch and displays a helpful message if the API call fails.

## Tech Stack

-   **Frontend**: [React](https://reactjs.org/), [Vite](https://vitejs.dev/)
-   **UI Library**: [Material-UI (MUI)](https://mui.com/)
-   **Linting**: [ESLint](https://eslint.org/)

## Prerequisites

Before you begin, ensure you have the following installed on your local machine:
-   [Node.js](https://nodejs.org/en/) (v16 or later recommended)
-   [npm](https://www.npmjs.com/) (which is included with Node.js)

## Getting Started

Follow these steps to get the application up and running on your local machine.

### 1. Clone the Repository

```bash
cd frontend/client
npm i
npm run dev
