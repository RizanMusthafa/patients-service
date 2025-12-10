import { useState, useEffect } from 'react';
import {
  DataGrid,
  GridActionsCellItem,
  GridRowModes,
  GridToolbar,
  type GridColDef,
  type GridRowId,
  type GridRowModel,
  type GridRowModesModel,
} from '@mui/x-data-grid';
import {
  Box,
  IconButton,
  Snackbar,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from '@mui/material';
import {
  Delete as DeleteIcon,
  Edit as EditIcon,
  Save as SaveIcon,
  Cancel as CancelIcon,
  Add as AddIcon,
} from '@mui/icons-material';
import type { Patient } from '../types/patient';
import { patientService } from '../services/patientService';

export default function PatientTable() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
  const [originalRows, setOriginalRows] = useState<Map<GridRowId, Patient>>(new Map());
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({
    open: false,
    message: '',
    severity: 'success',
  });
  const [deleteDialog, setDeleteDialog] = useState<{
    open: boolean;
    patientId: GridRowId | null;
    patientName: string;
  }>({
    open: false,
    patientId: null,
    patientName: '',
  });

  useEffect(() => {
    loadPatients();
  }, []);

  const loadPatients = async () => {
    try {
      setLoading(true);
      const data = await patientService.getAll();
      setPatients(data);
    } catch (error) {
      showSnackbar('Failed to load patients', 'error');
    } finally {
      setLoading(false);
    }
  };

  const showSnackbar = (message: string, severity: 'success' | 'error') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleRowEditStart = (params: { id: GridRowId }) => {
    const originalRow = patients.find((row) => row.id === params.id);
    if (originalRow) {
      setOriginalRows(new Map(originalRows.set(params.id, { ...originalRow })));
    }
    setRowModesModel({
      ...rowModesModel,
      [params.id]: { mode: GridRowModes.Edit },
    });
  };

  const handleRowEditStop = (params: { id: GridRowId }) => {
    setRowModesModel({
      ...rowModesModel,
      [params.id]: { mode: GridRowModes.View },
    });
  };

  const handleEditClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } });
  };

  const handleSaveClick = (id: GridRowId) => () => {
    setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } });
  };

  const handleCancelClick = (id: GridRowId) => () => {
    setRowModesModel({
      ...rowModesModel,
      [id]: { mode: GridRowModes.View, ignoreModifications: true },
    });
    originalRows.delete(id);
    const editedRow = patients.find((row) => row.id === id);
    if (editedRow && typeof id === 'string' && id.startsWith('new-')) {
      setPatients(patients.filter((row) => row.id !== id));
    }
  };

  const processRowUpdate = async (
    newRow: GridRowModel
  ): Promise<GridRowModel> => {
    try {
      if (!newRow.firstName || !newRow.lastName) {
        throw new Error('First name and last name are required');
      }

      if (!newRow.phoneNumber && !newRow.email) {
        throw new Error('Either phone number or email is required');
      }

      const rowId =
        typeof newRow.id === 'string' && newRow.id.startsWith('new-')
          ? null
          : newRow.id;

      if (rowId && typeof rowId === 'number') {
        const originalRow = originalRows.get(rowId);
        const changedFields: Partial<Patient> = {};

        if (originalRow) {
          if (newRow.firstName !== originalRow.firstName) {
            changedFields.firstName = newRow.firstName as string;
          }
          if (newRow.lastName !== originalRow.lastName) {
            changedFields.lastName = newRow.lastName as string;
          }
          if (newRow.address !== (originalRow.address || '')) {
            changedFields.address = newRow.address as string;
          }
          if (newRow.city !== (originalRow.city || '')) {
            changedFields.city = newRow.city as string;
          }
          if (newRow.state !== (originalRow.state || '')) {
            changedFields.state = newRow.state as string;
          }
          if (newRow.zipCode !== (originalRow.zipCode || '')) {
            changedFields.zipCode = newRow.zipCode as string;
          }
          if (newRow.phoneNumber !== (originalRow.phoneNumber || '')) {
            changedFields.phoneNumber = newRow.phoneNumber as string;
          }
          if (newRow.email !== (originalRow.email || '')) {
            changedFields.email = newRow.email as string;
          }
        } else {
          changedFields.firstName = newRow.firstName as string;
          changedFields.lastName = newRow.lastName as string;
          changedFields.address = newRow.address as string;
          changedFields.city = newRow.city as string;
          changedFields.state = newRow.state as string;
          changedFields.zipCode = newRow.zipCode as string;
          changedFields.phoneNumber = newRow.phoneNumber as string;
          changedFields.email = newRow.email as string;
        }

        const updated = await patientService.patch(rowId, changedFields);
        setPatients(patients.map((row) => (row.id === rowId ? updated : row)));
        originalRows.delete(rowId);
        showSnackbar('Patient updated successfully', 'success');
        return updated;
      } else {
        const patientData: Patient = {
          firstName: newRow.firstName as string,
          lastName: newRow.lastName as string,
          address: newRow.address as string || '',
          city: newRow.city as string || '',
          state: newRow.state as string || '',
          zipCode: newRow.zipCode as string || '',
          phoneNumber: newRow.phoneNumber as string || '',
          email: newRow.email as string || '',
        };

        const created = await patientService.create(patientData);
        setPatients(
          patients
            .filter(
              (row) =>
                !(typeof row.id === 'string' && row.id.startsWith('new-'))
            )
            .concat(created)
        );
        showSnackbar('Patient created successfully', 'success');
        return created;
      }
    } catch (error) {
      showSnackbar(
        error instanceof Error ? error.message : 'Failed to save patient',
        'error'
      );
      throw error;
    }
  };

  const handleDeleteClick = (id: GridRowId) => () => {
    const patient = patients.find((row) => row.id === id);
    const patientName = patient
      ? `${patient.firstName} ${patient.lastName}`.trim() || 'this patient'
      : 'this patient';
    setDeleteDialog({
      open: true,
      patientId: id,
      patientName,
    });
  };

  const handleConfirmDelete = async () => {
    if (!deleteDialog.patientId) return;

    try {
      await patientService.delete(deleteDialog.patientId as number);
      setPatients(patients.filter((row) => row.id !== deleteDialog.patientId));
      showSnackbar('Patient deleted successfully', 'success');
      setDeleteDialog({ open: false, patientId: null, patientName: '' });
    } catch (error) {
      showSnackbar('Failed to delete patient', 'error');
    }
  };

  const handleCancelDelete = () => {
    setDeleteDialog({ open: false, patientId: null, patientName: '' });
  };

  const handleAddRow = () => {
    const newId = `new-${Date.now()}`;
    const newRow: Patient = {
      id: newId as any,
      firstName: '',
      lastName: '',
      address: '',
      city: '',
      state: '',
      zipCode: '',
      phoneNumber: '',
      email: '',
    };
    setPatients([newRow, ...patients]);
    setRowModesModel({
      ...rowModesModel,
      [newId]: { mode: GridRowModes.Edit },
    });
  };

  const columns: GridColDef[] = [
    {
      field: 'firstName',
      headerName: 'First Name',
      width: 150,
      editable: true,
      flex: 1,
    },
    {
      field: 'lastName',
      headerName: 'Last Name',
      width: 150,
      editable: true,
      flex: 1,
    },
    {
      field: 'address',
      headerName: 'Address',
      width: 200,
      editable: true,
      flex: 1,
    },
    {
      field: 'city',
      headerName: 'City',
      width: 120,
      editable: true,
      flex: 1,
    },
    {
      field: 'state',
      headerName: 'State',
      width: 120,
      editable: true,
      flex: 1,
    },
    {
      field: 'zipCode',
      headerName: 'Zip Code',
      width: 120,
      editable: true,
      flex: 1,
    },
    {
      field: 'phoneNumber',
      headerName: 'Phone',
      width: 150,
      editable: true,
      flex: 1,
    },
    {
      field: 'email',
      headerName: 'Email',
      width: 200,
      editable: true,
      flex: 1,
    },
    {
      field: 'actions',
      type: 'actions',
      headerName: 'Actions',
      width: 100,
      cellClassName: 'actions',
      getActions: ({ id, row }) => {
        const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

        if (isInEditMode) {
          return [
            <GridActionsCellItem
              icon={<SaveIcon />}
              label="Save"
              onClick={handleSaveClick(id)}
              color="primary"
            />,
            <GridActionsCellItem
              icon={<CancelIcon />}
              label="Cancel"
              onClick={handleCancelClick(id)}
              color="inherit"
            />,
          ];
        }

        return [
          <GridActionsCellItem
            icon={<EditIcon />}
            label="Edit"
            onClick={handleEditClick(id)}
            color="primary"
          />,
          <GridActionsCellItem
            icon={<DeleteIcon />}
            label="Delete"
            onClick={handleDeleteClick(id)}
            color="error"
          />,
        ];
      },
    },
  ];

  return (
    <Box sx={{ height: 600, width: '100%' }}>
      <Box
        sx={{
          mb: 2,
          display: 'flex',
          justifyContent: 'flex-end',
          alignItems: 'center',
          gap: 1,
        }}
      >
        <IconButton onClick={handleAddRow} color="primary" size="large">
          <AddIcon />
        </IconButton>
      </Box>
      <DataGrid
        rows={patients}
        columns={columns}
        editMode="row"
        rowModesModel={rowModesModel}
        onRowModesModelChange={setRowModesModel}
        onRowEditStart={handleRowEditStart}
        onRowEditStop={handleRowEditStop}
        processRowUpdate={processRowUpdate}
        loading={loading}
        getRowId={(row) => row.id || `temp-${Math.random()}`}
        slots={{
          toolbar: GridToolbar,
        }}
        slotProps={{
          toolbar: {
            showQuickFilter: true,
          },
        }}
        sx={{
          '& .MuiDataGrid-cell:focus': {
            outline: 'none',
          },
        }}
      />
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>

      <Dialog
        open={deleteDialog.open}
        onClose={handleCancelDelete}
        aria-labelledby="delete-dialog-title"
        aria-describedby="delete-dialog-description"
      >
        <DialogTitle id="delete-dialog-title">Delete Patient</DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Are you sure you want to delete <strong>{deleteDialog.patientName}</strong>? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCancelDelete} color="inherit">
            Cancel
          </Button>
          <Button onClick={handleConfirmDelete} color="error" variant="contained" autoFocus>
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
