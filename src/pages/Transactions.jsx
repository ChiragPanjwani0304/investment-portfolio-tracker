import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function Transactions() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [transactions, setTransactions] = useState([]);
    const [form, setForm] = useState({ symbol: '', type: 'BUY', quantity: '', pricePerUnit: '', notes: '' });

    const fetchTransactions = async () => {
        const res = await api.get(`/portfolios/${id}/transactions`);
        setTransactions(res.data);
    };

    useEffect(() => { fetchTransactions(); }, [id]);

    const logTransaction = async () => {
        if (!form.symbol || !form.quantity || !form.pricePerUnit) return;
        await api.post(`/portfolios/${id}/transactions`, form);
        setForm({ symbol: '', type: 'BUY', quantity: '', pricePerUnit: '', notes: '' });
        fetchTransactions();
    };

    return (
        <div style={styles.page}>
            <button style={styles.back} onClick={() => navigate(`/portfolio/${id}`)}>← Back</button>
            <h2>Transaction History</h2>

            <div style={styles.form}>
                <input style={styles.input} placeholder="Symbol"
                    value={form.symbol} onChange={e => setForm({ ...form, symbol: e.target.value })} />
                <select style={styles.input} value={form.type}
                    onChange={e => setForm({ ...form, type: e.target.value })}>
                    <option value="BUY">BUY</option>
                    <option value="SELL">SELL</option>
                </select>
                <input style={styles.input} placeholder="Quantity"
                    value={form.quantity} onChange={e => setForm({ ...form, quantity: e.target.value })} />
                <input style={styles.input} placeholder="Price per unit"
                    value={form.pricePerUnit} onChange={e => setForm({ ...form, pricePerUnit: e.target.value })} />
                <input style={styles.input} placeholder="Notes (optional)"
                    value={form.notes} onChange={e => setForm({ ...form, notes: e.target.value })} />
                <button style={styles.btn} onClick={logTransaction}>Log</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        {['Symbol', 'Type', 'Qty', 'Price', 'Total', 'Date', 'Notes'].map(h => (
                            <th key={h} style={styles.th}>{h}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {transactions.map(t => (
                        <tr key={t.id}>
                            <td style={styles.td}>{t.symbol}</td>
                            <td style={{ ...styles.td, color: t.type === 'BUY' ? 'green' : 'red', fontWeight: 'bold' }}>{t.type}</td>
                            <td style={styles.td}>{t.quantity}</td>
                            <td style={styles.td}>${parseFloat(t.pricePerUnit).toFixed(2)}</td>
                            <td style={styles.td}>${parseFloat(t.totalAmount).toFixed(2)}</td>
                            <td style={styles.td}>{new Date(t.transactionDate).toLocaleDateString()}</td>
                            <td style={styles.td}>{t.notes || '—'}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
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
    td: { padding: '10px 14px', borderBottom: '1px solid #eee', fontSize: '14px' }
};