export default function HoldingRow({ holding, gainLoss, onDelete }) {
    const gl = gainLoss ? parseFloat(gainLoss.unrealizedGainLoss) : null;
    const glPct = gainLoss ? parseFloat(gainLoss.unrealizedGainLossPercent) : null;
    const isPositive = gl >= 0;

    return (
        <tr>
            <td style={styles.td}>{holding.symbol}</td>
            <td style={styles.td}>{holding.companyName || '—'}</td>
            <td style={styles.td}>{holding.quantity}</td>
            <td style={styles.td}>${parseFloat(holding.averageBuyPrice).toFixed(2)}</td>
            <td style={styles.td}>{gainLoss ? `$${parseFloat(gainLoss.currentPrice).toFixed(2)}` : '—'}</td>
            <td style={{ ...styles.td, color: isPositive ? 'green' : 'red' }}>
                {gl !== null ? `$${gl.toFixed(2)} (${glPct.toFixed(2)}%)` : '—'}
            </td>
            <td style={styles.td}>
                <button style={styles.deleteBtn} onClick={onDelete}>Remove</button>
            </td>
        </tr>
    );
}

const styles = {
    td: { padding: '10px 14px', borderBottom: '1px solid #eee', fontSize: '14px' },
    deleteBtn: { padding: '4px 10px', backgroundColor: '#e94560', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }
};