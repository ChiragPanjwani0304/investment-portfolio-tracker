import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function Alerts() {
    const [alerts, setAlerts] = useState([]);
    const [form, setForm] = useState({ symbol: '', targetPrice: '', conditionType: 'ABOVE' });

    const fetchAlerts = async () => {
        const res = await api.get('/alerts');
        setAlerts(res.data);
    };

    useEffect(() => { fetchAlerts(); }, []);

    const create = async () => {
        if (!form.symbol || !form.targetPrice) return;
        await api.post('/alerts', form);
        setForm({ symbol: '', targetPrice: '', conditionType: 'ABOVE' });
        fetchAlerts();
    };

    const remove = async (id) => {
        await api.delete(`/alerts/${id}`);
        fetchAlerts();
    };

    return (
        <div style={styles.page}>
            <h2>Price Alerts</h2>

            <div style={styles.form}>
                <input style={styles.input} placeholder="Symbol"
                    value={form.symbol} onChange={e => setForm({ ...form, symbol: e.target.value })} />
                <input style={styles.input} placeholder="Target price"
                    value={form.targetPrice} onChange={e => setForm({ ...form, targetPrice: e.target.value })} />
                <select style={styles.input} value={form.conditionType}
                    onChange={e => setForm({ ...form, conditionType: e.target.value })}>
                    <option value="ABOVE">ABOVE</option>
                    <option value="BELOW">BELOW</option>
                </select>
                <button style={styles.btn} onClick={create}>+ Create Alert</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        {['Symbol', 'Condition', 'Target Price', 'Status', 'Triggered At', 'Action'].map(h => (
                            <th key={h} style={styles.th}>{h}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {alerts.map(a => (
                        <tr key={a.id}>
                            <td style={styles.td}>{a.symbol}</td>
                            <td style={styles.td}>{a.conditionType}</td>
                            <td style={styles.td}>${parseFloat(a.targetPrice).toFixed(2)}</td>
                            <td style={{ ...styles.td, color: a.isTriggered ? 'green' : '#888' }}>
                                {a.isTriggered ? 'Triggered' : 'Watching'}
                            </td>
                            <td style={styles.td}>
                                {a.triggeredAt ? new Date(a.triggeredAt).toLocaleDateString() : '—'}
                            </td>
                            <td style={styles.td}>
                                <button style={styles.deleteBtn} onClick={() => remove(a.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

const styles = {
    page: { padding: '32px' },
    form: { display: 'flex', gap: '10px', marginBottom: '24px' },
    input: { padding: '8px', border: '1px solid #ddd', borderRadius: '4px' },
    btn: { padding: '8px 20px', backgroundColor: '#1a1a2e', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    table: { width: '100%', borderCollapse: 'collapse', backgroundColor: 'white', borderRadius: '8px', overflow: 'hidden' },
    th: { padding: '12px 14px', backgroundColor: '#1a1a2e', color: 'white', textAlign: 'left', fontSize: '14px' },
    td: { padding: '10px 14px', borderBottom: '1px solid #eee', fontSize: '14px' },
    deleteBtn: { padding: '4px 10px', backgroundColor: '#e94560', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }
};