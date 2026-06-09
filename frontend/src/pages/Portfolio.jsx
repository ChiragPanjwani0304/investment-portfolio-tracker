import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axios';
import HoldingRow from '../components/HoldingRow';
import AllocationChart from '../components/AllocationChart';
import GainLossChart from '../components/GainLossChart';

export default function Portfolio() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [holdings, setHoldings] = useState([]);
    const [gainLossData, setGainLossData] = useState([]);
    const [form, setForm] = useState({ symbol: '', companyName: '', quantity: '', averageBuyPrice: '', sector: '' });

    const fetchHoldings = async () => {
        const res = await api.get(`/portfolios/${id}/holdings`);
        setHoldings(res.data);
    };

    const fetchGainLoss = async () => {
        try {
            const res = await api.get(`/portfolios/${id}/transactions/gain-loss`);
            setGainLossData(res.data);
        } catch (e) {
            setGainLossData([]);
        }
    };

    useEffect(() => {
        fetchHoldings();
        fetchGainLoss();
    }, [id]);

    const addHolding = async () => {
        if (!form.symbol || !form.quantity || !form.averageBuyPrice) return;
        await api.post(`/portfolios/${id}/holdings`, form);
        setForm({ symbol: '', companyName: '', quantity: '', averageBuyPrice: '', sector: '' });
        fetchHoldings();
        fetchGainLoss();
    };

    const deleteHolding = async (holdingId) => {
        await api.delete(`/portfolios/${id}/holdings/${holdingId}`);
        fetchHoldings();
        fetchGainLoss();
    };

    const getGainLoss = (symbol) => gainLossData.find(g => g.symbol === symbol) || null;

    return (
        <div style={styles.page}>
            <button style={styles.back} onClick={() => navigate('/')}>← Back</button>
            <h2>Holdings</h2>

            <div style={styles.form}>
                {['symbol', 'companyName', 'quantity', 'averageBuyPrice', 'sector'].map(field => (
                    <input key={field} style={styles.input} placeholder={field}
                        value={form[field]}
                        onChange={e => setForm({ ...form, [field]: e.target.value })} />
                ))}
                <button style={styles.btn} onClick={addHolding}>+ Add Holding</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        {['Symbol', 'Company', 'Qty', 'Avg Buy', 'Current', 'Gain/Loss', 'Action'].map(h => (
                            <th key={h} style={styles.th}>{h}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {holdings.map(h => (
                        <HoldingRow key={h.id} holding={h}
                            gainLoss={getGainLoss(h.symbol)}
                            onDelete={() => deleteHolding(h.id)} />
                    ))}
                </tbody>
            </table>

            <div style={styles.charts}>
                <AllocationChart holdings={holdings} />
                <GainLossChart gainLossData={gainLossData} />
            </div>

            <button style={{ ...styles.btn, marginTop: '24px' }}
                onClick={() => navigate(`/portfolio/${id}/transactions`)}>
                View Transactions
            </button>
        </div>
    );
}

const styles = {
    page: { padding: '32px' },
    back: { marginBottom: '16px', background: 'none', border: 'none', cursor: 'pointer', color: '#1a1a2e', fontSize: '14px' },
    form: { display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '24px' },
    input: { padding: '8px', border: '1px solid #ddd', borderRadius: '4px' },
    btn: { padding: '8px 20px', backgroundColor: '#1a1a2e', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    table: { width: '100%', borderCollapse: 'collapse', backgroundColor: 'white', borderRadius: '8px', overflow: 'hidden' },
    th: { padding: '12px 14px', backgroundColor: '#1a1a2e', color: 'white', textAlign: 'left', fontSize: '14px' },
    charts: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px', marginTop: '40px' }
};