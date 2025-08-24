import {Router} from 'express';

const router:Router = Router();

// Hello World route
router.get('/', (req, res) => {
    res.json({"status": 200, "message": "This is root route of user API"});
});

export default router;

