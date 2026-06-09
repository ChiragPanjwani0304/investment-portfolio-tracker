import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#a855f7', '#ec4899', '#14b8a6'];

export default function AllocationChart({ holdings }) {
    const data = holdings.map(h => ({
        name: h.symbol,
        value: parseFloat((parseFloat(h.quantity) * parseFloat(h.averageBuyPrice)).toFixed(2))
    }));

    if (data.length === 0) return null;

    return (
        <div style={{ width: '100%', height: 300 }}>
            <h3>Portfolio Allocation</h3>
            <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                    <Pie data={data} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={100} label>
                        {data.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                    </Pie>
                    <Tooltip formatter={(val) => `$${val}`} />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
}