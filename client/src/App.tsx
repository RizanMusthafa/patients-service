import { Container, Typography, Box } from '@mui/material';
import PatientTable from './components/PatientTable';

function App() {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ mb: 1 }}>
        <Typography variant="h4" component="h1">
          Patient Management
        </Typography>
      </Box>
      <PatientTable />
    </Container>
  );
}

export default App;
