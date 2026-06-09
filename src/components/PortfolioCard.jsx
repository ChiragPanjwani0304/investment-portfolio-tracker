export default function PortfolioCard({ portfolio, onOpen, onDelete }) {
    return (
        <div style={styles.card}>
            <h3 style={styles.name}>{portfolio.name}</h3>
            <p style={styles.desc}>{portfolio.description || 'No description'}</p>
            <div style={styles.actions}>
                <button style={styles.openBtn} onClick={onOpen}>View</button>
                <button style={styles.deleteBtn} onClick={onDelete}>Delete</button>
            </div>
        </div>
    );
}

const styles = {
    card: { backgroundColor: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.08)' },
    name: { margin: '0 0 8px 0', fontSize: '18px' },
    desc: { color: '#666', fontSize: '14px', marginBottom: '20px' },
    actions: { display: 'flex', gap: '10px' },
    openBtn: { flex: 1, padding: '8px', backgroundColor: '#1a1a2e', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    deleteBtn: { flex: 1, padding: '8px', backgroundColor: '#e94560', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }
};