import express, {Express, json, NextFunction, Request, Response} from 'express';
import userRoutes from "./routes/users/user.routes";
import authenticationRoutes from "./routes/auth/authentication.routes";
import morgan from "morgan";

// Init express app
const app: Express = express();
const PORT: number = parseInt(process?.env?.PORT || '8098');

// Middleware for logging requests
app.use(morgan('combined'));
app.use(express.json());
app.disable('etag');

// Hello World route
app.get('/', (req, res) => {
    res.json({"status": 200, "message": "Hello World!"});
});

// register Routes
app.use("/api/users", userRoutes);
app.use("/api/auth", authenticationRoutes);


app.listen(PORT, () => {
    console.log(`Server is listening on ${PORT}`);
})
