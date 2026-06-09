import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';

export default function GainLossChart({ gainLossData }) {
    if (!gainLossData || gainLossData.length === 0) return null;

    const data = gainLossData.map(g => ({
        symbol: g.symbol,
        gainLoss: parseFloat(parseFloat(g.unrealizedGainLoss).toFixed(2))
    }));

    return (
        <div style={{ width: '100%', height: 300 }}>
            <h3>Unrealized Gain / Loss</h3>
            <ResponsiveContainer width="100%" height="100%">
                <BarChart data={data}>
                    <XAxis dataKey="symbol" />
                    <YAxis />
                    <Tooltip formatter={(val) => `$${val}`} />
                    <Bar dataKey="gainLoss">
                        {data.map((entry, i) => (
                            <Cell key={i} fill={entry.gainLoss >= 0 ? '#00C49F' : '#e94560'} />
                        ))}
                    </Bar>
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
}