import com.example.absencemanager.models
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/login/")
    fun login(@Body loginRequest: models.LoginRequest): Call<models.LoginResponse>
    @GET("api/professor/{userId}/classrooms/")
    public abstract fun getClassrooms(@Path("userId") userId: Int): Call<List<models.Classroom>>

    @GET("dashboard/{userId}/")
    fun getDashboardData(@Path("userId") userId: Int): Call<models.DashboardData>

    @GET("api/classroom/{classroom_id}/students/")
    fun getStudents(@Path("classroom_id") classroomId: Int): Call<List<models.Student>>

    @GET("api/student/{student_id}/absences/")
    fun getAbsences(@Path("student_id") studentId: Int): Call<List<models.Absence>>

    //@GET("api/student/{student_id}/motivations/")
    //fun getMotivations(@Path("student_id") studentId: Int): Call<List<models.Motivation>>
    //abstract fun getClassrooms(): Call<List<models.Classroom>>

    @Multipart
    @POST("api/detect-student/")  // Changed from "/detect_student" to "api/detect-student/"
    suspend fun detectStudent(@Part image: MultipartBody.Part): Response<models.DetectionResult>
}