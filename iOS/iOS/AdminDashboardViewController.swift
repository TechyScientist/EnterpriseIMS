//
//  AdminDashboardViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-27.
//
import UIKit

class AdminDashboardViewController: UIViewController {
    
    private var user: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    func with(_ adminUser: User) {
        user = adminUser
    }
    
    @IBAction func onBack() {
        dismiss(animated: true)
    }
    
    @IBAction func onAddUser() {
        performSegue(withIdentifier: "ShowAddUser", sender: nil)
    }
    
    @IBAction func onDeleteUser() {
        performSegue(withIdentifier: "ShowDeleteUser", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "ShowAddUser") {
            let destinationVC = segue.destination as! AddUserViewController
            destinationVC.with(user!)
        }
        else if(segue.identifier == "ShowDeleteUser") {
            let destinationVC = segue.destination as! DeleteUserViewController
            destinationVC.with(user!)
        }
    }
}

