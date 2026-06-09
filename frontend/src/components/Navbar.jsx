import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav style={styles.nav}>
            <Link to="/" style={styles.brand}>PortfolioTracker</Link>
            <div style={styles.links}>
                <Link to="/" style={styles.link}>Dashboard</Link>
                <Link to="/watchlist" style={styles.link}>Watchlist</Link>
                <Link to="/alerts" style={styles.link}>Alerts</Link>
                <span style={styles.username}>{user?.username}</span>
                <button onClick={handleLogout} style={styles.btn}>Logout</button>
            </div>
        </nav>
    );
}

const styles = {
    nav: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '12px 32px',
        backgroundColor: '#1a1a2e',
        color: 'white',
    },
    brand: {
        color: 'white',
        textDecoration: 'none',
        fontSize: '20px',
        fontWeight: 'bold',
    },
    links: {
        display: 'flex',
        alignItems: 'center',
        gap: '20px',
    },
    link: {
        color: 'white',
        textDecoration: 'none',
        fontSize: '14px',
    },
    username: {
        color: '#a0a0b0',
        fontSize: '14px',
    },
    btn: {
        padding: '6px 14px',
        backgroundColor: '#e94560',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
    }
};