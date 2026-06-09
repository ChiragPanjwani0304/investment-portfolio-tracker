import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function WatchList() {
    const [watchlist, setWatchlist] = useState([]);
    const [form, setForm] = useState({ symbol: '', companyName: '' });

    const fetchWatchlist = async () => {
        const res = await api.get('/watchlist');
        setWatchlist(res.data);
    };

    useEffect(() => { fetchWatchlist(); }, []);

    const add = async () => {
        if (!form.symbol) return;
        await api.post('/watchlist', form);
        setForm({ symbol: '', companyName: '' });
        fetchWatchlist();
    };

    const remove = async (id) => {
        await api.delete(`/watchlist/${id}`);
        fetchWatchlist();
    };

    return (
        <div style={styles.page}>
            <h2>Watchlist</h2>

            <div style={styles.form}>
                <input style={styles.input} placeholder="Symbol"
                    value={form.symbol} onChange={e => setForm({ ...form, symbol: e.target.value })} />
                <input style={styles.input} placeholder="Company name"
                    value={form.companyName} onChange={e => setForm({ ...form, companyName: e.target.value })} />
                <button style={styles.btn} onClick={add}>+ Add</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        {['Symbol', 'Company', 'Added', 'Action'].map(h => (
                            <th key={h} style={styles.th}>{h}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {watchlist.map(w => (
                        <tr key={w.id}>
                            <td style={styles.td}>{w.symbol}</td>
                            <td style={styles.td}>{w.companyName || '—'}</td>
                            <td style={styles.td}>{new Date(w.addedAt).toLocaleDateString()}</td>
                            <td style={styles.td}>
                                <button style={styles.deleteBtn} onClick={() => remove(w.id)}>Remove</button>
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