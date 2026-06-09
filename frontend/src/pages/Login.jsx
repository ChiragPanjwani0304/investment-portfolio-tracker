import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function Login() {
    const [form, setForm] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', form);
            login(res.data);
            navigate('/');
        } catch (err) {
            setError('Invalid credentials');
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>Login</h2>
                {error && <p style={styles.error}>{error}</p>}
                <input
                    style={styles.input}
                    placeholder="Username"
                    value={form.username}
                    onChange={e => setForm({ ...form, username: e.target.value })}
                />
                <input
                    style={styles.input}
                    placeholder="Password"
                    type="password"
                    value={form.password}
                    onChange={e => setForm({ ...form, password: e.target.value })}
                />
                <button style={styles.btn} onClick={handleSubmit}>Login</button>
                <p style={styles.sub}>No account? <Link to="/register">Register</Link></p>
            </div>
        </div>
    );
}

const styles = {
    container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#f0f2f5' },
    card: { backgroundColor: 'white', padding: '40px', borderRadius: '8px', width: '360px', boxShadow: '0 2px 12px rgba(0,0,0,0.1)' },
    title: { marginBottom: '24px', textAlign: 'center' },
    input: { display: 'block', width: '100%', padding: '10px', marginBottom: '16px', border: '1px solid #ddd', borderRadius: '4px', boxSizing: 'border-box' },
    btn: { width: '100%', padding: '10px', backgroundColor: '#1a1a2e', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    error: { color: 'red', marginBottom: '12px', fontSize: '14px' },
    sub: { textAlign: 'center', marginTop: '16px', fontSize: '14px' }
};