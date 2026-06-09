import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import PortfolioCard from '../components/PortfolioCard';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
    const [portfolios, setPortfolios] = useState([]);
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const navigate = useNavigate();
    const { user } = useAuth();

    const fetchPortfolios = async () => {
        try {
            const res = await api.get('/portfolios');
            setPortfolios(Array.isArray(res.data) ? res.data : []);
        } catch (e) {
            setPortfolios([]);
        }
    };

    useEffect(() => {
        if (user) {
            fetchPortfolios();
        }
    }, [user]);

    const createPortfolio = async () => {
        if (!name.trim()) return;
        await api.post('/portfolios', { name, description });
        setName('');
        setDescription('');
        fetchPortfolios();
    };

    const deletePortfolio = async (id) => {
        await api.delete(`/portfolios/${id}`);
        fetchPortfolios();
    };

    return (
        <div style={styles.page}>
            <h2>My Portfolios</h2>

            <div style={styles.form}>
                <input style={styles.input} placeholder="Portfolio name"
                    value={name} onChange={e => setName(e.target.value)} />
                <input style={styles.input} placeholder="Description (optional)"
                    value={description} onChange={e => setDescription(e.target.value)} />
                <button style={styles.btn} onClick={createPortfolio}>+ Create</button>
            </div>

            <div style={styles.grid}>
                {portfolios.map(p => (
                    <PortfolioCard
                        key={p.id}
                        portfolio={p}
                        onOpen={() => navigate(`/portfolio/${p.id}`)}
                        onDelete={() => deletePortfolio(p.id)}
                    />
                ))}
            </div>
        </div>
    );
}

const styles = {
    page: { padding: '32px' },
    form: { display: 'flex', gap: '12px', marginBottom: '32px', flexWrap: 'wrap' },
    input: { padding: '10px', border: '1px solid #ddd', borderRadius: '4px', minWidth: '200px' },
    btn: { padding: '10px 20px', backgroundColor: '#1a1a2e', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }
};