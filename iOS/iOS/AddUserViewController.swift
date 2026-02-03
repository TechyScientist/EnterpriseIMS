//
//  AddUserViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-28.
//
import UIKit

class AddUserViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var successView: UIView!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var lbSuccess: UILabel!
    @IBOutlet weak var lbError: UILabel!
    @IBOutlet weak var tfUsername: UITextField!
    @IBOutlet weak var tfName: UITextField!
    @IBOutlet weak var tfPassword: UITextField!
    @IBOutlet weak var tfConfirmPassword: UITextField!
    @IBOutlet weak var btIsAdmin: UIButton!
    @IBOutlet weak var btSubmit: UIButton!
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    
    private var user: User?
    private var isUserAdmin = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    func with(_ adminUser: User) {
        user = adminUser
    }
    
    @IBAction func onBack() {
        let controller = UIAlertController(title: "Confirm Exit", message: "Are you sure you want to exit? Your changes will be lost.", preferredStyle: .alert)
        controller.addAction(UIAlertAction(title: "No", style: .cancel))
        controller.addAction(UIAlertAction(title: "Yes", style: .destructive) {_ in
            self.dismiss(animated: true)
        })
        present(controller, animated: true)
    }
    
    @IBAction func onSubmit() {
        indicator.isHidden = false
        errorView.isHidden = true
        successView.isHidden = true
        btSubmit.isEnabled = false
        
        var request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/user/add")!)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.httpBody = "username=\(tfUsername.text?.lowercased() ?? "")&name=\(tfName.text ?? "")&password=\(tfPassword.text ?? "")&confirm-password=\(tfConfirmPassword.text ?? "")&is-admin=\(isUserAdmin)&auth-user=\(user!.username)".data(using: .utf8)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) {[self] _, response, _ in
            let responseCode = (response as! HTTPURLResponse).statusCode
            
            DispatchQueue.main.async {
                self.indicator.isHidden = true
                if(responseCode == CREATED) {
                    self.successView.isHidden = false
                    self.lbSuccess.attributedText = NSAttributedString(string: "User \(self.tfUsername.text!) added successfully.", attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))])
                    self.tfUsername.text = ""
                    self.tfName.text = ""
                    self.tfPassword.text = ""
                    self.tfConfirmPassword.text = ""
                    (self.btIsAdmin.menu!.children.first as! UICommand).state = .on
                    self.isUserAdmin = false
                }
                else {
                    let errorText = NSMutableAttributedString(string: "Error", attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))])
                    
                    if(responseCode == BAD_REQUEST) {
                        errorText.append(NSAttributedString(string: ": Missing or empty parameter, please try again.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    // theoretically should never happen if you got this far...
                    else if(responseCode == NOT_FOUND) {
                        errorText.append(NSAttributedString(string: ": User \(self.user!.username) not found.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    // theoretically should never happen if you got this far...
                    else if(responseCode == UNAUTHORIZED) {
                        errorText.append(NSAttributedString(string: ": User \(self.user!.username) is not an administrator.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    else if(responseCode == CONFLICT) {
                        errorText.append(NSAttributedString(string: ": User \(self.tfUsername.text!.lowercased()) already exists, please try a different username.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    else if(responseCode == UNPROCESSABLE) {
                        errorText.append(NSAttributedString(string: ": Your passwords do not match, please try again.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    else {
                        errorText.append(NSAttributedString(string: ": Unexpected HTTP response code: \(responseCode). ", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    }
                    self.errorView.isHidden = false
                    self.lbError.attributedText = errorText
                }
                self.indicator.isHidden = true
                self.btSubmit.isEnabled = true
            }
        }.resume()
    }
    
    @IBAction func onSelectAdmin() {
        isUserAdmin = true
    }
    
    @IBAction func onSelectNonAdmin() {
        isUserAdmin = false
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if(textField == tfUsername) {
            tfName.becomeFirstResponder()
        }
        else if(textField == tfName) {
            tfPassword.becomeFirstResponder()
        }
        else if(textField == tfPassword) {
            tfConfirmPassword.becomeFirstResponder()
        }
        else {
            tfConfirmPassword.resignFirstResponder()
        }
        return true
    }
}
