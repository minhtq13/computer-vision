import {Router} from 'express';

const router:Router = Router();
const routePrefix:string = '/api/auth';

router.get('/', (req, res) => {
    res.json({"status": 200, "message": "This is root route of authentication API"});
});

export default router;

