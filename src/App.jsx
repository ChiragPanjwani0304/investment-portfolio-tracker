import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Portfolio from './pages/Portfolio';
import Transactions from './pages/Transactions';
import WatchList from './pages/WatchList';
import Alerts from './pages/Alerts';

function PrivateRoute({ children }) {
    const { user } = useAuth();
    return user ? children : <Navigate to="/login" />;
}

function AppRoutes() {
    const { user } = useAuth();

    return (
        <>
            {user && <Navbar />}
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
                <Route path="/portfolio/:id" element={<PrivateRoute><Portfolio /></PrivateRoute>} />
                <Route path="/portfolio/:id/transactions" element={<PrivateRoute><Transactions /></PrivateRoute>} />
                <Route path="/watchlist" element={<PrivateRoute><WatchList /></PrivateRoute>} />
                <Route path="/alerts" element={<PrivateRoute><Alerts /></PrivateRoute>} />
            </Routes>
        </>
    );
}

export default function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <AppRoutes />
            </BrowserRouter>
        </AuthProvider>
    );
}