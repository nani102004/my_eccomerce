import {
  Grid,
  TextField,
  Button,
  Snackbar,
  Alert,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  Box,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { getUser, register } from "../../../Redux/Auth/Action";
import { useEffect, useState } from "react";

export default function RegisterUserForm({ handleNext }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { auth } = useSelector((store) => store);

  const [openSnackBar, setOpenSnackBar] = useState(false);
  const [role, setRole] = useState(""); // FIX: controlled Select

  const jwt = localStorage.getItem("jwt");

  const handleClose = () => setOpenSnackBar(false);

  // fetch user if jwt exists
  useEffect(() => {
    if (jwt) {
      dispatch(getUser(jwt));
    }
  }, [jwt, dispatch]);

  // open snackbar when user registers or error occurs
  useEffect(() => {
    if (auth.user || auth.error) setOpenSnackBar(true);
  }, [auth.user, auth.error]);

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const userData = {
      firstName: data.get("firstName"),
      lastName: data.get("lastName"),
      email: data.get("email"),
      password: data.get("password"),
      role: role, // from state
    };

    console.log("user data", userData);
    dispatch(register(userData));
  };

  return (
    <div className="">
      <Box component="form" onSubmit={handleSubmit}>
        <Grid container spacing={3}>
          {/* First Name */}
          <Grid item xs={12} sm={6}>
            <TextField
              required
              id="firstName"
              name="firstName"
              label="First Name"
              fullWidth
              autoComplete="given-name"
            />
          </Grid>

          {/* Last Name */}
          <Grid item xs={12} sm={6}>
            <TextField
              required
              id="lastName"
              name="lastName"
              label="Last Name"
              fullWidth
              autoComplete="family-name"
            />
          </Grid>

          {/* Email */}
          <Grid item xs={12}>
            <TextField
              required
              id="email"
              name="email"
              label="Email"
              fullWidth
              autoComplete="email"
            />
          </Grid>

          {/* Role Select */}
          <Grid item xs={12}>
            <FormControl fullWidth>
              <InputLabel id="role-label">Role</InputLabel>
              <Select
                labelId="role-label"
                id="role"
                name="role"
                value={role}
                onChange={(e) => setRole(e.target.value)}
              >
                <MenuItem value="ROLE_ADMIN">Admin</MenuItem>
                <MenuItem value="ROLE_CUSTOMER">Customer</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          {/* Password */}
          <Grid item xs={12}>
            <TextField
              required
              id="password"
              name="password"
              label="Password"
              fullWidth
              autoComplete="new-password"
              type="password"
            />
          </Grid>

          {/* Submit Button */}
          <Grid item xs={12}>
            <Button
              className="bg-[#9155FD] w-full"
              type="submit"
              variant="contained"
              size="large"
              sx={{ padding: ".8rem 0" }}
            >
              Register
            </Button>
          </Grid>
        </Grid>
      </Box>

      {/* Already have account */}
      <div className="flex justify-center flex-col items-center">
        <div className="py-3 flex items-center">
          <p className="m-0 p-0">Already have an account?</p>
          <Button
            onClick={() => navigate("/login")}
            className="ml-5"
            size="small"
          >
            Login
          </Button>
        </div>
      </div>

      {/* Snackbar */}
      <Snackbar open={openSnackBar} autoHideDuration={6000} onClose={handleClose}>
        <Alert
          onClose={handleClose}
          severity={auth.error ? "error" : "success"}
          sx={{ width: "100%" }}
        >
          {auth.error
            ? auth.error
            : auth.user
            ? "Register Success"
            : ""}
        </Alert>
      </Snackbar>
    </div>
  );
}
